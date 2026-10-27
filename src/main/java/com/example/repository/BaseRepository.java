package com.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T, ID> {

    @PersistenceContext(unitName = "myPU")
    protected EntityManager em;

    private final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        em.persist(entity);
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityName() + " e ORDER BY e.id", entityClass)
                .getResultList();
    }

    public T merge(T entity) {
        return em.merge(entity);
    }

    public void remove(T entity) {
        em.remove(entity);
    }

    public List<T> findPage(int number, int size) {
        return em.createQuery("SELECT e FROM " + entityName() + " e ORDER BY e.id", entityClass)
                .setFirstResult(number * size)
                .setMaxResults(size)
                .getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(e) FROM " + entityName() + " e", Long.class)
                .getSingleResult();
    }

    private String entityName() {
        return entityClass.getSimpleName();
    }
}
