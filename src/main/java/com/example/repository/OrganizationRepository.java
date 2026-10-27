package com.example.repository;

import com.example.entity.Organization;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganizationRepository extends BaseRepository<Organization, Long> {
    public OrganizationRepository() {
        super(Organization.class);
    }

    public long countWorkers(Organization organization) {
        return em.createQuery("SELECT COUNT(w) FROM Worker w WHERE w.organization = :organization", Long.class)
                .setParameter("organization", organization)
                .getSingleResult();
    }
}
