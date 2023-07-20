package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Role;
import com.kapok.erp.organization.entities.RolePermission;
import com.kapok.erp.organization.entities.RolePermissionId;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.inputs.RoleGrantParam;
import com.kapok.erp.organization.repositories.RolePermissionRepository;
import com.kapok.erp.organization.repositories.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RolePermissionRepository rolePermissionRepository;

	@Override
	public Page<Role> queryList(Role role, QueryParam queryParam, Pageable pageable) {
		ExampleMatcher matcher;
		if (StringUtils.isNotBlank(queryParam.getKeyword())) {
			role.setCode(queryParam.getKeyword());
			role.setName(queryParam.getKeyword());
			matcher = ExampleMatcher.matchingAny();
		} else {
			matcher = ExampleMatcher.matchingAll();
		}
		matcher = matcher.withMatcher("name", match -> match.startsWith());
		Example<Role> example = Example.of(role, matcher);
		Page<Role> page = roleRepository.findAll(example, pageable);
		return page;
	}

	@Override
	public Optional<Role> findBy(Integer id) {
		return roleRepository.findById(id);
	}

	@Transactional
	@Override
	public void save(Role role) {
		roleRepository.save(role);
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		if (id == null || id <= 1) {
			return;
		}
		roleRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void deleteAll(List<Integer> ids) {
		ids = ids.stream().filter(id -> id != null && id > 1).toList();
		roleRepository.deleteAllById(ids);
	}

	@Override
	public List<Role> searchList(QueryParam queryParam) {
		List<Role> list = roleRepository.findTop10ByCodeOrNameContaining(
				queryParam.getKeyword(), queryParam.getKeyword());
		return list;
	}

	@Override
	public Optional<RoleGrantParam> granted(Integer id) {
		Optional<Role> role = roleRepository.findById(id);
		Optional<RoleGrantParam> roleGrantParam = role.map(r -> {
			List<Integer> permIds = rolePermissionRepository.findPermIdByRoleId(r.getId());
			RoleGrantParam param = new RoleGrantParam();
			param.setId(r.getId());
			param.setCode(r.getCode());
			param.setName(r.getName());
			param.setEnabled(r.getEnabled());
			param.setPermIds(permIds);
			return param;
		});
		return roleGrantParam;
	}

	@Transactional
	@Override
	public void grant(RoleGrantParam roleGrantParam) {
		Integer roleId = roleGrantParam.getId();
		List<Integer> permIds = roleGrantParam.getPermIds();
		List<Integer> existsPermIds = rolePermissionRepository.findPermIdByRoleId(roleGrantParam.getId());
		List<RolePermissionId> deleteList = existsPermIds.stream()
				.filter(permId -> !permIds.contains(permId))
				.map(permId -> {
					RolePermissionId id = new RolePermissionId();
					id.setRoleId(roleId);
					id.setPermId(permId);
					return id;
				})
				.toList();
		List<RolePermission> saveList = permIds.stream()
				.filter(permId -> !existsPermIds.contains(permId))
				.map(permId -> {
					RolePermissionId id = new RolePermissionId();
					id.setRoleId(roleGrantParam.getId());
					id.setPermId(permId);
					RolePermission rolePermission = new RolePermission();
					rolePermission.setId(id);
					return rolePermission;
				})
				.toList();
		rolePermissionRepository.deleteAllById(deleteList);
		rolePermissionRepository.saveAll(saveList);
	}
}
