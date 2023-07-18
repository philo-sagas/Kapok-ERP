package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.DictionaryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryItemRepository extends JpaRepository<DictionaryItem, Integer> {
}