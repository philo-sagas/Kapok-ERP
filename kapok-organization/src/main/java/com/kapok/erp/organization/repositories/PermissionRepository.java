package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}