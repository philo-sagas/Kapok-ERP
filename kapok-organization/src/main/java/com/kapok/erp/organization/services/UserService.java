package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> queryList(User user, Pageable pageable);

    Optional<User> findBy(Integer id);

    void save(User user);

    void delete(Integer id);
}
