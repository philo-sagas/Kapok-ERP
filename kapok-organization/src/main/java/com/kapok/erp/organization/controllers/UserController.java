package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.User;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/user")
public class UserController {
	@Autowired
	private UserService userService;

	@PreAuthorize("hasAuthority('user:query')")
	@GetMapping
	public PagedModel<User> query(User user, QueryParam queryParam, Pageable pageable) {
		Page<User> page = userService.queryList(user, queryParam, pageable);
		return PagedModel.success(page);
	}

	@PreAuthorize("hasAnyAuthority('user:query', 'user:save')")
	@GetMapping("/{id}")
	public ResultModel<User> find(@PathVariable("id") Integer id) {
		Optional<User> user = userService.findBy(id);
		return ResultModel.success(user.orElse(null));
	}

	@PreAuthorize("hasAuthority('user:save')")
	@PostMapping
	public ResultModel<Void> save(@RequestBody User user) {
		userService.save(user);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('user:delete')")
	@DeleteMapping("/{id}")
	public ResultModel<Void> delete(@PathVariable("id") Integer id) {
		userService.delete(id);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('user:delete')")
	@DeleteMapping
	public ResultModel<Void> deleteAll(@RequestBody List<Integer> ids) {
		userService.deleteAll(ids);
		return ResultModel.success();
	}
}
