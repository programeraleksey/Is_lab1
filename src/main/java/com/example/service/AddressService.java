package com.example.service;

import com.example.entity.Address;
import com.example.repository.BaseRepository;
import com.example.repository.AddressRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AddressService extends BaseService<Address, Long> {
    @Inject
    private AddressRepository repo;

    @Override
    protected BaseRepository<Address, Long> repository() {
        return repo;
    }

    @Override
    protected String entityName() {
        return "Адрес";
    }

    @Override
    protected void check(Address address) {
        ensureNotUsed(address, repo.countOrganizations(address), "организации");
    }
}
