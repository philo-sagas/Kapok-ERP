package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.Organization;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

	@PreAuthorize("hasAuthority('organization:query')")
    @GetMapping
    public PagedModel<Organization> query(Organization organization, Pageable pageable) {
        Page<Organization> page = organizationService.queryList(organization, pageable);
        return PagedModel.success(page);
    }

    @GetMapping("/{id}")
    public ResultModel<Organization> find(@PathVariable("id") Integer id) {
        Optional<Organization> user = organizationService.findBy(id);
        return ResultModel.success(user.orElseGet(null));
    }

    @PostMapping
    public ResultModel<Void> save(@RequestBody Organization organization) {
        organizationService.save(organization);
        return ResultModel.success(null);
    }

    @DeleteMapping("/{id}")
    public ResultModel<Void> delete(@PathVariable("id") Integer id) {
        organizationService.delete(id);
        return ResultModel.success(null);
    }
}
