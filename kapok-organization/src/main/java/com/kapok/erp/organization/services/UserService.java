package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.User;
import com.kapok.erp.organization.inputs.QueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
	Page<User> queryList(User user, QueryParam queryParam, Pageable pageable);

	Optional<User> findBy(Integer id);

	void save(User user);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);
}
