package com.kapok.erp.organization.services;

import com.kapok.erp.organization.constants.ApplicationConstants;
import com.kapok.erp.organization.entities.Organization;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.TreeNode;
import com.kapok.erp.organization.repositories.OrganizationRepository;
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
public class OrganizationServiceImpl implements OrganizationService {
	@Autowired
	private OrganizationRepository organizationRepository;

	@Override
	public Page<Organization> queryList(Organization organization, QueryParam queryParam, Pageable pageable) {
		ExampleMatcher matcher;
		if (StringUtils.isNotBlank(queryParam.getKeyword())) {
			organization.setCode(queryParam.getKeyword());
			organization.setName(queryParam.getKeyword());
			matcher = ExampleMatcher.matchingAny();
		} else {
			matcher = ExampleMatcher.matchingAll();
		}
		matcher = matcher.withMatcher("name", match -> match.startsWith())
				.withMatcher("pCode", match -> match.startsWith());
		Example<Organization> example = Example.of(organization, matcher);
		Page<Organization> page = organizationRepository.findAll(example, pageable);
		return page;
	}

	@Override
	public Optional<Organization> findBy(Integer id) {
		return organizationRepository.findById(id);
	}

	@Transactional
	@Override
	public void save(Organization organization) {
		Optional<Organization> parent;
		if (organization.getPid() != null) {
			parent = organizationRepository.findById(organization.getPid());
		} else {
			parent = Optional.empty();
		}
		parent.ifPresentOrElse(p -> {
			organization.setPCode(p.getPCode() + ApplicationConstants.PLACEHOLDER + organization.getCode());
		}, () -> {
			organization.setPid(0);
			organization.setPCode(organization.getCode());
		});
		organization.setLeaf(true);
		organizationRepository.save(organization);
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		if (id == null || id <= 9) {
			return;
		}
		organizationRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void deleteAll(List<Integer> ids) {
		ids = ids.stream().filter(id -> id != null && id > 9).toList();
		organizationRepository.deleteAllById(ids);
	}

	@Override
	public List<TreeNode> buildTree() {
		TreeNode root = new TreeNode();
		root.setId(0);
		Queue<TreeNode> queue = new LinkedList();
		queue.offer(root);
		while (!queue.isEmpty()) {
			TreeNode node = queue.poll();
			List<Organization> list = organizationRepository.findByPid(node.getId());
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
	public List<Organization> searchList(QueryParam queryParam) {
		List<Organization> list = organizationRepository.findTop10ByCodeOrNameContaining(
				queryParam.getKeyword(), queryParam.getKeyword());
		return list;
	}
}
