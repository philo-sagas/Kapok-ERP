package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}