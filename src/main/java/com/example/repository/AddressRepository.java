package com.example.repository;

import com.example.entity.Address;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository extends BaseRepository<Address, Long> {
    public AddressRepository() {
        super(Address.class);
    }

    public long countOrganizations(Address address) {
        return em.createQuery("SELECT COUNT(o) FROM Organization o WHERE o.officialAddress = :address", Long.class)
                .setParameter("address", address)
                .getSingleResult();
    }
}
