package com.example.service;

import com.example.entity.Coordinates;
import com.example.repository.BaseRepository;
import com.example.repository.CoordinatesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CoordinatesService extends BaseService<Coordinates, Long> {
    @Inject
    private CoordinatesRepository repo;

    @Override
    protected BaseRepository<Coordinates, Long> repository() {
        return repo;
    }

    @Override
    protected String entityName() {
        return "Координаты";
    }

    @Override
    protected void check(Coordinates coordinates) {
        ensureNotUsed(coordinates, repo.countWorkers(coordinates), "работники");
    }
}
