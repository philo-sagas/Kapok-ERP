package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PermissionService {
    Page<Permission> queryList(Permission permission, Pageable pageable);

    Optional<Permission> findBy(Integer id);

    void save(Permission permission);

    void delete(Integer id);
}
