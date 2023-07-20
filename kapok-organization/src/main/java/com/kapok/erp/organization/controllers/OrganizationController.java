package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.Organization;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.outputs.TreeNode;
import com.kapok.erp.organization.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/organization")
public class OrganizationController {
	@Autowired
	private OrganizationService organizationService;

	@PreAuthorize("hasAuthority('organization:query')")
	@GetMapping
	public PagedModel<Organization> query(Organization organization, QueryParam queryParam, Pageable pageable) {
		Page<Organization> page = organizationService.queryList(organization, queryParam, pageable);
		return PagedModel.success(page);
	}

	@PreAuthorize("hasAuthority('organization:query')")
	@GetMapping("/search")
	public ResultModel<List<Organization>> search(QueryParam queryParam) {
		List<Organization> list = organizationService.searchList(queryParam);
		return ResultModel.success(list);
	}

	@PreAuthorize("hasAuthority('organization:query')")
	@GetMapping("/tree")
	public ResultModel<List<TreeNode>> tree() {
		List<TreeNode> list = organizationService.buildTree();
		return ResultModel.success(list);
	}

	@PreAuthorize("hasAnyAuthority('organization:query', 'organization:save')")
	@GetMapping("/{id}")
	public ResultModel<Organization> find(@PathVariable("id") Integer id) {
		Optional<Organization> user = organizationService.findBy(id);
		return ResultModel.success(user.orElse(null));
	}

	@PreAuthorize("hasAuthority('organization:save')")
	@PostMapping
	public ResultModel<Void> save(@RequestBody Organization organization) {
		organizationService.save(organization);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('organization:delete')")
	@DeleteMapping("/{id}")
	public ResultModel<Void> delete(@PathVariable("id") Integer id) {
		organizationService.delete(id);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('organization:delete')")
	@DeleteMapping
	public ResultModel<Void> deleteAll(@RequestBody List<Integer> ids) {
		organizationService.deleteAll(ids);
		return ResultModel.success();
	}
}
