package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {
	Dictionary findTopByCode(String code);
}
