package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}