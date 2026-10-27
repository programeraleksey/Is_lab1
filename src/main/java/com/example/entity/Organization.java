package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "organization")
public class Organization implements Serializable, Identifiable<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "official_address_id", nullable = true)
    private Address officialAddress;

    @Positive(message = "Значение поля должно быть больше 0")
    @Column(name = "annual_turnover", nullable = false)
    private int annualTurnover;

    @Positive(message = "Значение поля должно быть больше 0")
    @Column(name = "employees_count", nullable = true)
    private Integer employeesCount;

    @NotNull(message = "Поле не может быть null")
    @NotBlank(message = "Строка не может быть пустой")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Positive(message = "Значение поля должно быть больше 0")
    @Column(name = "rating", nullable = false)
    private long rating;

    public Organization() {
    }

    public Organization(Address officialAddress, int annualTurnover, Integer employeesCount, String fullName, long rating) {
        this.officialAddress = officialAddress;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
        this.fullName = fullName;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    public int getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(int annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public Integer getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(Integer employeesCount) {
        this.employeesCount = employeesCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", officialAddress=" + officialAddress +
                ", annualTurnover=" + annualTurnover +
                ", employeesCount=" + employeesCount +
                ", fullName='" + fullName + '\'' +
                ", rating=" + rating +
                '}';
    }
}