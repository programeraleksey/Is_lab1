package com.example.service;

import com.example.entity.Organization;
import com.example.repository.BaseRepository;
import com.example.repository.OrganizationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrganizationService extends BaseService<Organization, Long> {
    @Inject
    private OrganizationRepository repo;

    @Override
    protected BaseRepository<Organization, Long> repository() {
        return repo;
    }

    @Override
    protected String entityName() {
        return "Организация";
    }

    @Override
    protected void check(Organization organization) {
        ensureNotUsed(organization, repo.countWorkers(organization), "работники");
    }
}
