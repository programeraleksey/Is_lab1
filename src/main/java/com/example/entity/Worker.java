package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "worker")
public class Worker implements Serializable, Identifiable<Integer> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Positive(message = "Значение поля должно быть больше 0")
    private Integer id;

    @NotNull(message = "Поле не может быть null")
    @NotBlank(message = "Строка не может быть пустой")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Поле не может быть null")
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @NotNull(message = "Поле не может быть null")
    @Column(name = "creation_date", nullable = false, updatable = false, columnDefinition = "timestamptz NOT NULL")
    private ZonedDateTime creationDate;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;

    @Positive(message = "Значение поля должно быть больше 0")
    @Column(name = "salary", nullable = false)
    private float salary;

    @Positive(message = "Значение поля должно быть больше 0")
    @Column(name = "rating", nullable = false)
    private int rating;

    @NotNull(message = "Поле не может быть null")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true, columnDefinition = "timestamptz")
    private ZonedDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = true)
    private Position position;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = true)
    private Person person;

    public Worker() {}

    public Worker(String name, Coordinates coordinates, Organization organization, float salary,
                  int rating, LocalDate startDate, ZonedDateTime endDate, Position position, Person person) {
        this.name = name;
        this.coordinates = coordinates;
        this.organization = organization;
        this.salary = salary;
        this.rating = rating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.person = person;
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = ZonedDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Worker worker)) return false;
        return Objects.equals(id, worker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", organization=" + organization +
                ", salary=" + salary +
                ", rating=" + rating +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", position=" + position +
                ", person=" + person +
                '}';
    }
}
