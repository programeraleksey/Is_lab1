package com.example.repository;

import com.example.entity.Location;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LocationRepository extends BaseRepository<Location, Long> {
    public LocationRepository() {
        super(Location.class);
    }

    public long countAddresses(Location location) {
        return em.createQuery("SELECT COUNT(a) FROM Address a WHERE a.town = :location", Long.class)
                .setParameter("location", location)
                .getSingleResult();
    }

    public long countPersons(Location location) {
        return em.createQuery("SELECT COUNT(p) FROM Person p WHERE p.location = :location", Long.class)
                .setParameter("location", location)
                .getSingleResult();
    }
}
