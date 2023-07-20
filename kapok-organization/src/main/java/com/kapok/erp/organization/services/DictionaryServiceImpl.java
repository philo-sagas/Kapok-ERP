package com.kapok.erp.organization.services;

import com.kapok.erp.organization.constants.ApplicationConstants;
import com.kapok.erp.organization.entities.Dictionary;
import com.kapok.erp.organization.entities.DictionaryItem;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.DictionaryDto;
import com.kapok.erp.organization.outputs.DictionaryItemDto;
import com.kapok.erp.organization.repositories.DictionaryItemRepository;
import com.kapok.erp.organization.repositories.DictionaryRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class DictionaryServiceImpl implements DictionaryService {
	@Autowired
	private DictionaryRepository dictionaryRepository;

	@Autowired
	private DictionaryItemRepository dictionaryItemRepository;

	@Override
	public Page<Dictionary> queryList(Dictionary dictionary, QueryParam queryParam, Pageable pageable) {
		ExampleMatcher matcher;
		if (StringUtils.isNotBlank(queryParam.getKeyword())) {
			dictionary.setCode(queryParam.getKeyword());
			dictionary.setName(queryParam.getKeyword());
			matcher = ExampleMatcher.matchingAny();
		} else {
			matcher = ExampleMatcher.matchingAll();
		}
		matcher = matcher.withMatcher("name", match -> match.startsWith());
		Example<Dictionary> example = Example.of(dictionary, matcher);
		Page<Dictionary> page = dictionaryRepository.findAll(example, pageable);
		return page;
	}

	@Override
	public Optional<Dictionary> findBy(Integer id) {
		return dictionaryRepository.findById(id);
	}

	@Override
	public Optional<DictionaryDto> findByWithItems(Integer id) {
		Optional<Dictionary> dictionary = dictionaryRepository.findById(id);
		Optional<DictionaryDto> dictionaryDto = dictionary.map(d -> {
			DictionaryDto dto = new DictionaryDto();
			BeanUtils.copyProperties(d, dto);
			List<DictionaryItem> items = dictionaryItemRepository.findByDictId(d.getId());
			dto.setItems(items);
			return dto;
		});
		return dictionaryDto;
	}

	@Transactional
	@Override
	public void save(DictionaryDto dictionaryDto) {
		Dictionary dictionary = new Dictionary();
		BeanUtils.copyProperties(dictionaryDto, dictionary);
		if (!ApplicationConstants.Dictionary.typeSingle.equals(dictionary.getType())) {
			dictionary.setValue(null);
		}
		dictionaryRepository.save(dictionary);
		if (ApplicationConstants.Dictionary.typeMulti.equals(dictionary.getType())) {
			List<DictionaryItem> saveList = dictionaryDto.getItems();
			List<DictionaryItem> existsItems = dictionaryItemRepository.findByDictId(dictionary.getId());
			List<DictionaryItem> deleteList = existsItems.stream()
					.filter(i -> saveList.stream().noneMatch(j -> i.getId().equals(j.getId())))
					.toList();
			saveList.stream().forEach(i -> {
				i.setDictId(dictionary.getId());
			});
			dictionaryItemRepository.deleteAll(deleteList);
			dictionaryItemRepository.saveAll(saveList);
		}
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		if (id == null || id <= 5) {
			return;
		}
		dictionaryRepository.deleteById(id);
		dictionaryItemRepository.deleteByDictId(id);
	}

	@Transactional
	@Override
	public void deleteAll(List<Integer> ids) {
		ids = ids.stream().filter(id -> id != null && id > 5).toList();
		ids.stream().forEach(id -> delete(id));
	}

	@Override
	public List<DictionaryItemDto> findItemsByCode(String code) {
		Dictionary dictionary = dictionaryRepository.findTopByCode(code);
		if (dictionary == null) {
			return Collections.emptyList();
		}
		if (ApplicationConstants.Dictionary.typeSingle.equals(dictionary.getType())) {
			return Arrays.asList(new DictionaryItemDto(code, dictionary.getValue(), 0));
		}
		List<DictionaryItemDto> dictionaryItemDtoList = dictionaryItemRepository.acquireItemsByDictId(dictionary.getId());
		return dictionaryItemDtoList;
	}
}
