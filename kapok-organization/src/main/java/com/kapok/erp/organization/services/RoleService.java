package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoleService {
    Page<Role> queryList(Role role, Pageable pageable);

    Optional<Role> findBy(Integer id);

    void save(Role role);

    void delete(Integer id);
}
