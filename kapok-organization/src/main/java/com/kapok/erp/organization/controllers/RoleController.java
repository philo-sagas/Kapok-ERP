package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.Role;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

	@PreAuthorize("hasAuthority('role:query')")
    @GetMapping
    public PagedModel<Role> query(Role role, Pageable pageable) {
        Page<Role> page = roleService.queryList(role, pageable);
        return PagedModel.success(page);
    }

    @GetMapping("/{id}")
    public ResultModel<Role> find(@PathVariable("id") Integer id) {
        Optional<Role> user = roleService.findBy(id);
        return ResultModel.success(user.orElseGet(null));
    }

    @PostMapping
    public ResultModel<Void> save(@RequestBody Role role) {
        roleService.save(role);
        return ResultModel.success(null);
    }

    @DeleteMapping("/{id}")
    public ResultModel<Void> delete(@PathVariable("id") Integer id) {
        roleService.delete(id);
        return ResultModel.success(null);
    }
}
