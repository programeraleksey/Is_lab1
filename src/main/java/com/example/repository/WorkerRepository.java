package com.example.repository;

import com.example.dto.WorkerFilter;
import com.example.entity.Organization;
import com.example.entity.Person;
import com.example.entity.Worker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WorkerRepository extends BaseRepository<Worker, Integer> {
    public WorkerRepository() {
        super(Worker.class);
    }

    public int deleteByPerson(Person person) {
        return em.createQuery("DELETE FROM Worker w WHERE w.person = :person")
                .setParameter("person", person)
                .executeUpdate();
    }

    public long countByEndDateAfter(ZonedDateTime date) {
        return em.createQuery("SELECT COUNT(w) FROM Worker w WHERE w.endDate > :date", Long.class)
                .setParameter("date", date)
                .getSingleResult();
    }

    public List<Worker> findByStartDateAfter(LocalDate date) {
        return em.createQuery("SELECT w FROM Worker w WHERE w.startDate > :date ORDER BY w.id", Worker.class)
                .setParameter("date", date)
                .getResultList();
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<Worker> w,
            Join<Worker, Organization> org,
            WorkerFilter filter) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getName() != null && !filter.getName().isBlank()) {
            predicates.add(cb.equal(w.get("name"), filter.getName()));
        }
        if (filter.getOrganizationName() != null && !filter.getOrganizationName().isBlank()) {
            predicates.add(cb.equal(org.get("fullName"), filter.getOrganizationName()));
        }
        if (filter.getPosition() != null) {
            predicates.add(cb.equal(w.get("position"), filter.getPosition()));
        }
        return predicates;
    }

    public List<Worker> findPage(WorkerFilter filter, int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Worker> cq = cb.createQuery(Worker.class);
        Root<Worker> w = cq.from(Worker.class);
        Join<Worker, Organization> org = w.join("organization", JoinType.LEFT);

        cq.where(buildPredicates(cb, w, org, filter).toArray(new Predicate[0]));

        Expression<?> sortPath = switch (filter.getSortField()) {
            case "name" -> w.get("name");
            case "organization" -> org.get("fullName");
            case "position" -> w.get("position");
            default -> w.get("id");
        };
        cq.orderBy(filter.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath), cb.asc(w.get("id")));

        return em.createQuery(cq)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public long count(WorkerFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Worker> w = cq.from(Worker.class);
        Join<Worker, Organization> org = w.join("organization", JoinType.LEFT);

        cq.select(cb.count(w));
        cq.where(buildPredicates(cb, w, org, filter).toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }
}
