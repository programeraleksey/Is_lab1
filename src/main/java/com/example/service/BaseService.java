package com.example.service;

import com.example.entity.Identifiable;
import com.example.exception.EntityInUseException;
import com.example.exception.NotFoundException;
import com.example.repository.BaseRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public abstract class BaseService<T extends Identifiable<ID>, ID> {

    protected abstract BaseRepository<T, ID> repository();

    protected abstract String entityName();

    @Transactional
    public void create(T entity) {
        repository().save(entity);
    }

    public T findById(ID id) {
        if (id == null) {
            throw new NotFoundException("Не указан id объекта " + entityName());
        }
        return repository().findById(id)
                .orElseThrow(() -> new NotFoundException("Объект " + entityName() + " с id " + id + " не найден"));
    }

    public List<T> findAll() {
        return repository().findAll();
    }

    @Transactional
    public T update(T entity) {
        findById(entity.getId());
        return repository().merge(entity);
    }

    @Transactional
    public void delete(ID id) {
        T entity = findById(id);
        check(entity);
        repository().remove(entity);
    }

    public List<T> findPage(int number, int size) {
        return repository().findPage(number, size);
    }

    public long count() {
        return repository().count();
    }

    protected void check(T entity) {}

    protected void ensureNotUsed(T entity, long count, String usedBy) {
        if (count > 0) {
            throw new EntityInUseException("Нельзя удалить объект " + entityName() + " с id " + entity.getId()
                    + ": на него ссылаются " + usedBy + " (" + count + ")");
        }
    }
}
