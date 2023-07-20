package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
	List<Organization> findByPid(Integer pid);

	List<Organization> findTop10ByCodeOrNameContaining(String code, String name);
}
