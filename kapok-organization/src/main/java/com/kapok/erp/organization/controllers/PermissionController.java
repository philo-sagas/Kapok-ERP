package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.Permission;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.outputs.TreeNode;
import com.kapok.erp.organization.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/permission")
public class PermissionController {
	@Autowired
	private PermissionService permissionService;

	@PreAuthorize("hasAuthority('permission:query')")
	@GetMapping
	public PagedModel<Permission> query(Permission permission, QueryParam queryParam, Pageable pageable) {
		Page<Permission> page = permissionService.queryList(permission, queryParam, pageable);
		return PagedModel.success(page);
	}

	@PreAuthorize("hasAuthority('permission:query')")
	@GetMapping("/search")
	public ResultModel<List<Permission>> search(QueryParam queryParam) {
		List<Permission> list = permissionService.searchList(queryParam);
		return ResultModel.success(list);
	}

	@PreAuthorize("hasAuthority('permission:query')")
	@GetMapping("/tree")
	public ResultModel<List<TreeNode>> tree() {
		List<TreeNode> list = permissionService.buildTree();
		return ResultModel.success(list);
	}

	@PreAuthorize("hasAnyAuthority('permission:query', 'permission:save')")
	@GetMapping("/{id}")
	public ResultModel<Permission> find(@PathVariable("id") Integer id) {
		Optional<Permission> permission = permissionService.findBy(id);
		return ResultModel.success(permission.orElse(null));
	}

	@PreAuthorize("hasAuthority('permission:save')")
	@PostMapping
	public ResultModel<Void> save(@RequestBody Permission permission) {
		permissionService.save(permission);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('permission:delete')")
	@DeleteMapping("/{id}")
	public ResultModel<Void> delete(@PathVariable("id") Integer id) {
		permissionService.delete(id);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('permission:delete')")
	@DeleteMapping
	public ResultModel<Void> deleteAll(@RequestBody List<Integer> ids) {
		permissionService.deleteAll(ids);
		return ResultModel.success();
	}
}
