package com.example.web;

import com.example.entity.Organization;
import com.example.entity.Person;
import com.example.entity.Worker;
import com.example.exception.BusinessException;
import com.example.service.OrganizationService;
import com.example.service.PersonService;
import com.example.service.WorkerService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Named
@ViewScoped
public class OperationsBean implements Serializable {

    @Inject
    private WorkerService workerService;
    @Inject
    private OrganizationService organizationService;
    @Inject
    private PersonService personService;

    private Long personId;
    private List<Person> allPersons;

    private LocalDateTime endDate;
    private Long endDateCount;

    private LocalDate startDate;
    private List<Worker> startDateWorkers;

    private Long organizationId;
    private List<Organization> allOrganizations;

    private Integer workerId;
    private List<Worker> allWorkers;

    private Float coefficient;

    @PostConstruct
    private void init() {
        loadLists();
    }

    private void loadLists() {
        allPersons = personService.findAll();
        allWorkers = workerService.findAll();
        allOrganizations = organizationService.findAll();
    }

    public void deleteByPerson() {
        try {
            int count = workerService.deleteByPerson(personId);
            info("Удалено работников с человеком №" + personId + ": " + count);
            loadLists();
        } catch (BusinessException e) {
            error(e.getMessage());
        } catch (RuntimeException e) {
            error(ErrorMessages.describe(e));
        }
    }

    public void indexSalary() {
        try {
            Worker worker = workerService.indexSalary(workerId, coefficient);
            info("Зарплата работника №" + worker.getId() + " теперь " + worker.getSalary());
            loadLists();
        } catch (BusinessException e) {
            error(e.getMessage());
        } catch (RuntimeException e) {
            error(ErrorMessages.describe(e));
        }
    }

    public void countByEndDateAfter() {
        try {
            endDateCount = workerService.countByEndDateAfter(endDate.atZone(ZoneId.systemDefault()));
        } catch (BusinessException e) {
            endDateCount = null;
            error(e.getMessage());
        } catch (RuntimeException e) {
            endDateCount = null;
            error(ErrorMessages.describe(e));
        }
    }

    public void findByStartDateAfter() {
        try {
            startDateWorkers = workerService.findByStartDateAfter(startDate);
        } catch (BusinessException e) {
            startDateWorkers = null;
            error(e.getMessage());
        } catch (RuntimeException e) {
            startDateWorkers = null;
            error(ErrorMessages.describe(e));
        }
    }

    public void hire() {
        try {
            Worker worker = workerService.hire(workerId, organizationId);
            info("Работник №" + worker.getId() + " принят в организацию «"
                    + worker.getOrganization().getFullName() + "»");
            loadLists();
        } catch (BusinessException e) {
            error(e.getMessage());
        } catch (RuntimeException e) {
            error(ErrorMessages.describe(e));
        }
    }

    private void info(String text) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(text));
    }

    private void error(String text) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, text, null));
    }

    public List<Person> getAllPersons() {
        return allPersons;
    }

    public List<Worker> getAllWorkers() {
        return allWorkers;
    }

    public List<Organization> getAllOrganizations() {
        return allOrganizations;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getEndDateCount() {
        return endDateCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<Worker> getStartDateWorkers() {
        return startDateWorkers;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
    }
}
