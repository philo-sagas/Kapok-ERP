package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Organization;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.TreeNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
	Page<Organization> queryList(Organization organization, QueryParam queryParam, Pageable pageable);

	Optional<Organization> findBy(Integer id);

	void save(Organization organization);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	List<TreeNode> buildTree();

	List<Organization> searchList(QueryParam queryParam);
}
