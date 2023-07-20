package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Permission;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.TreeNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
	Page<Permission> queryList(Permission permission, QueryParam queryParam, Pageable pageable);

	Optional<Permission> findBy(Integer id);

	void save(Permission permission);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	List<TreeNode> buildTree();

	List<Permission> searchList(QueryParam queryParam);
}
