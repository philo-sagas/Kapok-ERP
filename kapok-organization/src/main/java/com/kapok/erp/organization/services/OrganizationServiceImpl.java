package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Organization;
import com.kapok.erp.organization.repositories.OrganizationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Page<Organization> queryList(Organization organization, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());
        Example<Organization> example = Example.of(organization, matcher);
        Page<Organization> page = organizationRepository.findAll(example, pageable);
        return page;
    }

    @Override
    public Optional<Organization> findBy(Integer id) {
        return organizationRepository.findById(id);
    }

    @Override
    public void save(Organization organization) {
        Optional<Organization> parent;
        if (organization.getPid() != null) {
            parent = organizationRepository.findById(organization.getPid());
        } else {
            parent = Optional.empty();
        }
        parent.ifPresentOrElse(o -> {
            organization.setPCode(o.getPCode());
        }, () -> {
            organization.setPid(0);
            organization.setPCode(organization.getCode());
        });
        organizationRepository.save(organization);
    }

    @Override
    public void delete(Integer id) {
        organizationRepository.deleteById(id);
    }
}
