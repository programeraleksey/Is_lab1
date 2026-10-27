package com.example.web;

import com.example.entity.Address;
import com.example.entity.Color;
import com.example.entity.Coordinates;
import com.example.entity.Country;
import com.example.entity.Location;
import com.example.entity.Organization;
import com.example.entity.Person;
import com.example.entity.Position;
import com.example.entity.Worker;
import com.example.exception.BusinessException;
import com.example.service.AddressService;
import com.example.service.CoordinatesService;
import com.example.service.LocationService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Named
@ViewScoped
public class WorkerFormBean implements Serializable {
    @Inject
    private WorkerService workerService;
    @Inject
    private CoordinatesService coordinatesService;
    @Inject
    private OrganizationService organizationService;
    @Inject
    private AddressService addressService;
    @Inject
    private LocationService locationService;
    @Inject
    private PersonService personService;

    private Integer id;
    private Worker worker;
    private LocalDateTime endDate;

    private Coordinates coordinates;
    private Long coordinatesId;
    private List<Coordinates> allCoordinates;
    private String coordinatesMode = "NEW";

    private Organization organization;
    private Long organizationId;
    private List<Organization> allOrganizations;
    private String organizationMode = "NONE";

    private Address address;
    private Long addressId;
    private List<Address> allAddresses;
    private String addressMode = "NONE";

    private Location town;
    private Long townId;
    private String townMode = "NEW";

    private Person person;
    private Long personId;
    private List<Person> allPersons;
    private String personMode = "NONE";

    private Location personLocation;
    private Long personLocationId;
    private String personLocationMode = "NONE";

    private List<Location> allLocations;

    @PostConstruct
    private void init() {
        worker = new Worker();
        coordinates = new Coordinates();
        organization = new Organization();
        address = new Address();
        town = new Location();
        person = new Person();
        personLocation = new Location();

        allCoordinates = coordinatesService.findAll();
        allOrganizations = organizationService.findAll();
        allAddresses = addressService.findAll();
        allLocations = locationService.findAll();
        allPersons = personService.findAll();
    }

    public void load() {
        if (id == null) {
            return;
        }
        try {
            worker = workerService.findById(id);
        } catch (BusinessException e) {
            worker = null;
            error(e.getMessage());
            return;
        } catch (RuntimeException e) {
            worker = null;
            error(ErrorMessages.describe(e));
            return;
        }
        if (worker.getEndDate() != null) {
            endDate = worker.getEndDate().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        }
        coordinatesMode = "EXISTING";
        coordinatesId = worker.getCoordinates().getId();
        if (worker.getOrganization() != null) {
            organizationMode = "EXISTING";
            organizationId = worker.getOrganization().getId();
        }
        if (worker.getPerson() != null) {
            personMode = "EXISTING";
            personId = worker.getPerson().getId();
        }
    }

    public boolean isEdit() {
        return id != null;
    }

    public String save() {
        try {
            worker.setEndDate(endDate == null ? null : endDate.atZone(ZoneId.systemDefault()));
            worker.setCoordinates(buildCoordinates());
            worker.setOrganization(buildOrganization());
            worker.setPerson(buildPerson());
            if (isEdit()) {
                workerService.update(worker);
            } else {
                workerService.create(worker);
            }
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(isEdit() ? "Работник изменён" : "Работник создан"));
            ctx.getExternalContext().getFlash().setKeepMessages(true);
            return "worker?id=" + worker.getId() + "&faces-redirect=true";
        } catch (BusinessException e) {
            error(e.getMessage());
            return null;
        } catch (RuntimeException e) {
            error(ErrorMessages.describe(e));
            return null;
        }
    }

    private void error(String text) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, text, null));
    }

    private Coordinates buildCoordinates() {
        if ("NEW".equals(coordinatesMode)) {
            return coordinates;
        }
        return coordinatesService.findById(coordinatesId);
    }

    private Organization buildOrganization() {
        return switch (organizationMode) {
            case "EXISTING" -> organizationService.findById(organizationId);
            case "NEW" -> {
                organization.setOfficialAddress(buildAddress());
                yield organization;
            }
            default -> null;
        };
    }

    private Address buildAddress() {
        return switch (addressMode) {
            case "EXISTING" -> addressService.findById(addressId);
            case "NEW" -> {
                address.setTown("NEW".equals(townMode) ? town : locationService.findById(townId));
                yield address;
            }
            default -> null;
        };
    }

    private Person buildPerson() {
        return switch (personMode) {
            case "EXISTING" -> personService.findById(personId);
            case "NEW" -> {
                person.setLocation(buildPersonLocation());
                yield person;
            }
            default -> null;
        };
    }

    private Location buildPersonLocation() {
        return switch (personLocationMode) {
            case "EXISTING" -> locationService.findById(personLocationId);
            case "NEW" -> personLocation;
            default -> null;
        };
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Worker getWorker() {
        return worker;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Position[] getPositions() {
        return Position.values();
    }

    public Color[] getColors() {
        return Color.values();
    }

    public Country[] getCountries() {
        return Country.values();
    }

    public List<Location> getAllLocations() {
        return allLocations;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Long getCoordinatesId() {
        return coordinatesId;
    }

    public void setCoordinatesId(Long coordinatesId) {
        this.coordinatesId = coordinatesId;
    }

    public List<Coordinates> getAllCoordinates() {
        return allCoordinates;
    }

    public String getCoordinatesMode() {
        return coordinatesMode;
    }

    public void setCoordinatesMode(String coordinatesMode) {
        this.coordinatesMode = coordinatesMode;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<Organization> getAllOrganizations() {
        return allOrganizations;
    }

    public String getOrganizationMode() {
        return organizationMode;
    }

    public void setOrganizationMode(String organizationMode) {
        this.organizationMode = organizationMode;
    }

    public Address getAddress() {
        return address;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<Address> getAllAddresses() {
        return allAddresses;
    }

    public String getAddressMode() {
        return addressMode;
    }

    public void setAddressMode(String addressMode) {
        this.addressMode = addressMode;
    }

    public Location getTown() {
        return town;
    }

    public Long getTownId() {
        return townId;
    }

    public void setTownId(Long townId) {
        this.townId = townId;
    }

    public String getTownMode() {
        return townMode;
    }

    public void setTownMode(String townMode) {
        this.townMode = townMode;
    }

    public Person getPerson() {
        return person;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public List<Person> getAllPersons() {
        return allPersons;
    }

    public String getPersonMode() {
        return personMode;
    }

    public void setPersonMode(String personMode) {
        this.personMode = personMode;
    }

    public Location getPersonLocation() {
        return personLocation;
    }

    public Long getPersonLocationId() {
        return personLocationId;
    }

    public void setPersonLocationId(Long personLocationId) {
        this.personLocationId = personLocationId;
    }

    public String getPersonLocationMode() {
        return personLocationMode;
    }

    public void setPersonLocationMode(String personLocationMode) {
        this.personLocationMode = personLocationMode;
    }
}
