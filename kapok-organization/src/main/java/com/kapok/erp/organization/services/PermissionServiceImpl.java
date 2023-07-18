package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Permission;
import com.kapok.erp.organization.repositories.PermissionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Page<Permission> queryList(Permission permission, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());
        Example<Permission> example = Example.of(permission, matcher);
        Page<Permission> page = permissionRepository.findAll(example, pageable);
        return page;
    }

    @Override
    public Optional<Permission> findBy(Integer id) {
        return permissionRepository.findById(id);
    }

    @Override
    public void save(Permission permission) {
        permissionRepository.save(permission);
    }

    @Override
    public void delete(Integer id) {
        permissionRepository.deleteById(id);
    }
}
