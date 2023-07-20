package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	List<Role> findTop10ByCodeOrNameContaining(String code, String name);
}
