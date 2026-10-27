package com.example.service;

import com.example.entity.Location;
import com.example.repository.BaseRepository;
import com.example.repository.LocationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LocationService extends BaseService<Location, Long> {
    @Inject
    private LocationRepository repo;

    @Override
    protected BaseRepository<Location, Long> repository() {
        return repo;
    }

    @Override
    protected String entityName() {
        return "Локация";
    }

    @Override
    protected void check(Location location) {
        ensureNotUsed(location, repo.countAddresses(location), "адреса");
        ensureNotUsed(location, repo.countPersons(location), "люди");
    }
}
