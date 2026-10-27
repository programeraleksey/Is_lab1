package com.example.service;

import com.example.dto.WorkerFilter;
import com.example.entity.Address;
import com.example.entity.Identifiable;
import com.example.entity.Organization;
import com.example.entity.Person;
import com.example.entity.Worker;
import com.example.exception.InvalidOperationException;
import com.example.repository.BaseRepository;
import com.example.repository.WorkerRepository;
import com.example.service.event.WorkersChanged;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
public class WorkerService extends BaseService<Worker, Integer> {
    @Inject
    private WorkerRepository repo;
    @Inject
    private PersonService personService;
    @Inject
    private OrganizationService organizationService;
    @Inject
    private CoordinatesService coordinatesService;
    @Inject
    private AddressService addressService;
    @Inject
    private LocationService locationService;
    @Inject
    private Event<WorkersChanged> workersChanged;

    @Override
    protected BaseRepository<Worker, Integer> repository() {
        return repo;
    }

    @Override
    @Transactional
    public void create(Worker worker) {
        attachRelations(worker);
        super.create(worker);
        workersChanged.fire(new WorkersChanged());
    }

    @Override
    @Transactional
    public Worker update(Worker worker) {
        Worker managed = findById(worker.getId());
        attachRelations(worker);
        managed.setName(worker.getName());
        managed.setCoordinates(worker.getCoordinates());
        managed.setOrganization(worker.getOrganization());
        managed.setSalary(worker.getSalary());
        managed.setRating(worker.getRating());
        managed.setStartDate(worker.getStartDate());
        managed.setEndDate(worker.getEndDate());
        managed.setPosition(worker.getPosition());
        managed.setPerson(worker.getPerson());
        workersChanged.fire(new WorkersChanged());
        return managed;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        super.delete(id);
        workersChanged.fire(new WorkersChanged());
    }

    private void attachRelations(Worker worker) {
        worker.setCoordinates(managed(worker.getCoordinates(), coordinatesService));

        Organization organization = managed(worker.getOrganization(), organizationService);
        if (organization != null && organization.getId() == null) {
            Address address = managed(organization.getOfficialAddress(), addressService);
            if (address != null && address.getId() == null) {
                address.setTown(managed(address.getTown(), locationService));
            }
            organization.setOfficialAddress(address);
        }
        worker.setOrganization(organization);

        Person person = managed(worker.getPerson(), personService);
        if (person != null && person.getId() == null) {
            person.setLocation(managed(person.getLocation(), locationService));
        }
        worker.setPerson(person);
    }

    private <T extends Identifiable<ID>, ID> T managed(T entity, BaseService<T, ID> service) {
        if (entity == null || entity.getId() == null) {
            return entity;
        }
        return service.findById(entity.getId());
    }

    @Override
    protected String entityName() {
        return "Работник";
    }

    @Transactional
    public int deleteByPerson(Long id) {
        int count = repo.deleteByPerson(personService.findById(id));
        workersChanged.fire(new WorkersChanged());
        return count;
    }

    public List<Worker> findPage(WorkerFilter filter, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new InvalidOperationException("Некорректная страница: номер " + page + ", размер " + size);
        }
        return repo.findPage(filter, page, size);
    }

    public long count(WorkerFilter filter) {
        return repo.count(filter);
    }

    public long countByEndDateAfter(ZonedDateTime date) {
        if (date == null) {
            throw new InvalidOperationException("Не указана дата окончания работы");
        }
        return repo.countByEndDateAfter(date);
    }

    public List<Worker> findByStartDateAfter(LocalDate date) {
        if (date == null) {
            throw new InvalidOperationException("Не указана дата начала работы");
        }
        return repo.findByStartDateAfter(date);
    }

    @Transactional
    public Worker hire(Integer workerId, Long organizationId) {
        Worker worker = findById(workerId);
        Organization organization = organizationService.findById(organizationId);
        if (organization.equals(worker.getOrganization())) {
            throw new InvalidOperationException("Работник с id " + workerId
                    + " уже работает в организации с id " + organizationId);
        }
        worker.setOrganization(organization);
        worker.setStartDate(LocalDate.now());
        worker.setEndDate(null);
        workersChanged.fire(new WorkersChanged());
        return worker;
    }

    @Transactional
    public Worker indexSalary(Integer workerId, float coefficient) {
        if (!(coefficient > 0)) {
            throw new InvalidOperationException("Коэффициент индексации должен быть больше 0");
        }
        Worker worker = findById(workerId);
        float newSalary = worker.getSalary() * coefficient;
        if (Float.isInfinite(newSalary) || newSalary <= 0) {
            throw new InvalidOperationException("Зарплата после индексации выходит за допустимые пределы: "
                    + worker.getSalary() + " * " + coefficient);
        }
        worker.setSalary(newSalary);
        workersChanged.fire(new WorkersChanged());
        return worker;
    }
}
