package com.example.service;

import com.example.entity.Person;
import com.example.repository.BaseRepository;
import com.example.repository.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PersonService extends BaseService<Person, Long> {
    @Inject
    private PersonRepository repo;

    @Override
    protected BaseRepository<Person, Long> repository() {
        return repo;
    }

    @Override
    protected String entityName() {
        return "Человек";
    }

    @Override
    protected void check(Person person) {
        ensureNotUsed(person, repo.countWorkers(person), "работники");
    }
}
