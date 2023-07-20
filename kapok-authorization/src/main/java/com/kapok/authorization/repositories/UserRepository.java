package com.kapok.authorization.repositories;

import com.kapok.authorization.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findTopBySubject(String subject);

	@Query(value = "SELECT p.code" +
			" FROM user u, role r, permission p, rolePermission rp" +
			" WHERE u.roleId = r.id AND r.id = rp.roleId AND p.id = rp.permId" +
			" AND r.enabled = true AND p.enabled = true AND u.id = ?1",
			nativeQuery = true)
	List<String> acquirePermCodeByUserId(Integer userId);

	@Query(value = "SELECT p.code FROM permission p WHERE p.enabled = true",
			nativeQuery = true)
	List<String> acquirePermCodeAll();
}
