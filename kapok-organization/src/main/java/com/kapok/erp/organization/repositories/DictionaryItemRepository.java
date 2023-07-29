package com.kapok.erp.organization.repositories;

import com.kapok.erp.organization.entities.DictionaryItem;
import com.kapok.erp.organization.outputs.DictionaryItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DictionaryItemRepository extends JpaRepository<DictionaryItem, Integer> {
	List<DictionaryItem> findByDictId(Integer dictId);

	void deleteByDictId(Integer dictId);

	@Query(value = "SELECT NEW com.kapok.erp.organization.outputs.DictionaryItemDto(di.code, di.value, di.rank)" +
			" FROM DictionaryItem di WHERE di.dictId = ?1")
	List<DictionaryItemDto> acquireItemsByDictId(Integer dictId);
}
