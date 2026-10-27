package com.example.repository;

import com.example.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonRepository extends BaseRepository<Person, Long> {
    public PersonRepository() {
        super(Person.class);
    }

    public long countWorkers(Person person) {
        return em.createQuery("SELECT COUNT(w) FROM Worker w WHERE w.person = :person", Long.class)
                .setParameter("person", person)
                .getSingleResult();
    }
}
