package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.RolePermission;
import com.kapok.erp.organization.entities.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
}