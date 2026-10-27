package com.example.repository;

import com.example.entity.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoordinatesRepository extends BaseRepository<Coordinates, Long> {
    public CoordinatesRepository() {
        super(Coordinates.class);
    }

    public long countWorkers(Coordinates coordinates) {
        return em.createQuery("SELECT COUNT(w) FROM Worker w WHERE w.coordinates = :coordinates", Long.class)
                .setParameter("coordinates", coordinates)
                .getSingleResult();
    }
}
