package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.Dictionary;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.outputs.DictionaryDto;
import com.kapok.erp.organization.outputs.DictionaryItemDto;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.services.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/dictionary")
public class DictionaryController {
	@Autowired
	private DictionaryService dictionaryService;

	@PreAuthorize("hasAuthority('dictionary:query')")
	@GetMapping
	public PagedModel<Dictionary> query(Dictionary dictionary, QueryParam queryParam, Pageable pageable) {
		Page<Dictionary> page = dictionaryService.queryList(dictionary, queryParam, pageable);
		return PagedModel.success(page);
	}

	@PreAuthorize("hasAnyAuthority('dictionary:query', 'dictionary:save')")
	@GetMapping("/{id}")
	public ResultModel<DictionaryDto> find(@PathVariable("id") Integer id) {
		Optional<DictionaryDto> dictionaryDto = dictionaryService.findByWithItems(id);
		return ResultModel.success(dictionaryDto.orElse(null));
	}

	@PreAuthorize("hasAuthority('dictionary:save')")
	@PostMapping
	public ResultModel<Void> save(@RequestBody DictionaryDto dictionaryDto) {
		dictionaryService.save(dictionaryDto);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('dictionary:delete')")
	@DeleteMapping("/{id}")
	public ResultModel<Void> delete(@PathVariable("id") Integer id) {
		dictionaryService.delete(id);
		return ResultModel.success();
	}

	@PreAuthorize("hasAuthority('dictionary:delete')")
	@DeleteMapping
	public ResultModel<Void> deleteAll(@RequestBody List<Integer> ids) {
		dictionaryService.deleteAll(ids);
		return ResultModel.success();
	}

	@GetMapping("/via/{code}")
	public ResultModel<Object> queryByCode(@PathVariable("code") String code) {
		List<DictionaryItemDto> list = dictionaryService.findItemsByCode(code);
		if (list.size() == 1 && list.get(1).getCode().equals(code)) {
			return ResultModel.success(list.get(1).getValue());
		} else if (!list.isEmpty()) {
			return ResultModel.success(list);
		}
		return ResultModel.success();
	}
}
