package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Dictionary;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.DictionaryDto;
import com.kapok.erp.organization.outputs.DictionaryItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DictionaryService {
	Page<Dictionary> queryList(Dictionary dictionary, QueryParam queryParam, Pageable pageable);

	Optional<Dictionary> findBy(Integer id);

	Optional<DictionaryDto> findByWithItems(Integer id);

	void save(DictionaryDto dictionaryDto);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	List<DictionaryItemDto> findItemsByCode(String code);
}
