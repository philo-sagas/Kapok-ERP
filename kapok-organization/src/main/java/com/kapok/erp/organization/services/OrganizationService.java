package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrganizationService {
    Page<Organization> queryList(Organization organization, Pageable pageable);

    Optional<Organization> findBy(Integer id);

    void save(Organization organization);

    void delete(Integer id);
}
