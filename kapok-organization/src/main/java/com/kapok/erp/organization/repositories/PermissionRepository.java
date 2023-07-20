package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
	List<Permission> findByPid(Integer pid);

	List<Permission> findTop10ByCodeOrNameContaining(String code, String name);
}
