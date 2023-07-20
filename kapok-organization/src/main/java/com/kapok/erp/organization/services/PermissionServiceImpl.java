package com.kapok.erp.organization.services;

import com.kapok.erp.organization.constants.ApplicationConstants;
import com.kapok.erp.organization.entities.Permission;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.TreeNode;
import com.kapok.erp.organization.repositories.PermissionRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public Page<Permission> queryList(Permission permission, QueryParam queryParam, Pageable pageable) {
		ExampleMatcher matcher;
		if (StringUtils.isNotBlank(queryParam.getKeyword())) {
			permission.setCode(queryParam.getKeyword());
			permission.setName(queryParam.getKeyword());
			matcher = ExampleMatcher.matchingAny();
		} else {
			matcher = ExampleMatcher.matchingAll();
		}
		matcher = matcher.withMatcher("name", match -> match.startsWith())
				.withMatcher("pCode", match -> match.startsWith());
		Example<Permission> example = Example.of(permission, matcher);
		Page<Permission> page = permissionRepository.findAll(example, pageable);
		return page;
	}

	@Override
	public Optional<Permission> findBy(Integer id) {
		return permissionRepository.findById(id);
	}

	@Transactional
	@Override
	public void save(Permission permission) {
		Optional<Permission> parent;
		if (permission.getPid() != null) {
			parent = permissionRepository.findById(permission.getPid());
		} else {
			parent = Optional.empty();
		}
		parent.ifPresentOrElse(p -> {
			permission.setPCode(p.getPCode() + ApplicationConstants.PLACEHOLDER + permission.getCode());
		}, () -> {
			permission.setPid(0);
			permission.setPCode(permission.getCode());
		});
		permission.setLeaf(true);
		permissionRepository.save(permission);
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		if (id == null || id <= 26) {
			return;
		}
		permissionRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void deleteAll(List<Integer> ids) {
		ids = ids.stream().filter(id -> id != null && id > 26).toList();
		permissionRepository.deleteAllById(ids);
	}

	@Override
	public List<TreeNode> buildTree() {
		TreeNode root = new TreeNode();
		root.setId(0);
		Queue<TreeNode> queue = new LinkedList();
		queue.offer(root);
		while (!queue.isEmpty()) {
			TreeNode node = queue.poll();
			List<Permission> list = permissionRepository.findByPid(node.getId());
			List<TreeNode> children = list.stream().map(o -> {
				TreeNode n = new TreeNode();
				n.setId(o.getId());
				n.setName(o.getName());
				n.setPath(o.getPCode());
				return n;
			}).collect(Collectors.toList());
			node.setChildren(children);
			node.setLeaf(children.isEmpty());
			queue.addAll(children);
		}
		return root.getChildren();
	}

	@Override
	public List<Permission> searchList(QueryParam queryParam) {
		List<Permission> list = permissionRepository.findTop10ByCodeOrNameContaining(
				queryParam.getKeyword(), queryParam.getKeyword());
		return list;
	}
}
