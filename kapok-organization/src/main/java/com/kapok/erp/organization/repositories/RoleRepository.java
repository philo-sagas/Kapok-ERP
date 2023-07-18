package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}