package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Role;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.inputs.RoleGrantParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoleService {
	Page<Role> queryList(Role role, QueryParam queryParam, Pageable pageable);

	Optional<Role> findBy(Integer id);

	void save(Role role);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	List<Role> searchList(QueryParam queryParam);

	Optional<RoleGrantParam> granted(Integer id);

	void grant(RoleGrantParam roleGrantParam);
}
