package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.Permission;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

	@PreAuthorize("hasAuthority('permission:query')")
    @GetMapping
    public PagedModel<Permission> query(Permission permission, Pageable pageable) {
        Page<Permission> page = permissionService.queryList(permission, pageable);
        return PagedModel.success(page);
    }

    @GetMapping("/{id}")
    public ResultModel<Permission> find(@PathVariable("id") Integer id) {
        Optional<Permission> user = permissionService.findBy(id);
        return ResultModel.success(user.orElseGet(null));
    }

    @PostMapping
    public ResultModel<Void> save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return ResultModel.success(null);
    }

    @DeleteMapping("/{id}")
    public ResultModel<Void> delete(@PathVariable("id") Integer id) {
        permissionService.delete(id);
        return ResultModel.success(null);
    }
}
