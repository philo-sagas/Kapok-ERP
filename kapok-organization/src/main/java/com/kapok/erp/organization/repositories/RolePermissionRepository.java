package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.RolePermission;
import com.kapok.erp.organization.entities.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
	@Query("SELECT NEW java.lang.Integer(rp.id.permId) FROM RolePermission rp WHERE rp.id.roleId = ?1")
	List<Integer> findPermIdByRoleId(Integer roleId);
}
