package com.example;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class NoteService {

    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    public Note add(String text) {
        Note n = new Note(text);
        em.persist(n);
        return n;
    }

    public List<Note> findAll() {
        return em.createQuery("SELECT n FROM Note n ORDER BY n.id", Note.class)
                .getResultList();
    }
}