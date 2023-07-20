package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.Role;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.inputs.RoleGrantParam;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/role")
public class RoleController {
	@Autowired
	private RoleService roleService;

	@PreAuthorize("hasAuthority('role:query')")
	@GetMapping
	public PagedModel<Role> query(Role role, QueryParam queryParam, Pageable pageable) {
		Page<Role> page = roleService.queryList(role, queryParam, pageable);
		return PagedModel.success(page);
	}

	@PreAuthorize("hasAuthority('role:query')")
	@GetMapping("/search")
	public ResultModel<List<Role>> search(QueryParam queryParam) {
		List<Role> list = roleService.searchList(queryParam);
		return ResultModel.success(list);
	}

	@PreAuthorize("hasAnyAuthority('role:query', 'role:save')")
	@GetMapping("/{id}")
	public ResultModel<Role> find(@PathVariable("id") Integer id) {
		Optional<Role> role = roleService.findBy(id);
		return ResultModel.success(role.orElse(null));
	}

	@PreAuthorize("hasAuthority('role:save')")
	@PostMapping
	public ResultModel<Void> save(@RequestBody Role role) {
		roleService.save(role);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('role:delete')")
	@DeleteMapping("/{id}")
	public ResultModel<Void> delete(@PathVariable("id") Integer id) {
		roleService.delete(id);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('role:delete')")
	@DeleteMapping
	public ResultModel<Void> deleteAll(@RequestBody List<Integer> ids) {
		roleService.deleteAll(ids);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('role:grant')")
	@GetMapping("/{id}/granted")
	public ResultModel<RoleGrantParam> granted(@PathVariable("id") Integer id) {
		Optional<RoleGrantParam> roleGrantParam = roleService.granted(id);
		return ResultModel.success(roleGrantParam.orElse(null));
	}

	@PreAuthorize("hasAuthority('role:grant')")
	@PostMapping("/grant")
	public ResultModel<Void> grant(@RequestBody RoleGrantParam roleGrantParam) {
		roleService.grant(roleGrantParam);
		return ResultModel.success(null);
	}
}
