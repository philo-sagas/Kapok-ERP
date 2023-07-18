package com.kapok.erp.organization.controllers;

import com.kapok.erp.organization.entities.Dictionary;
import com.kapok.erp.organization.outputs.PagedModel;
import com.kapok.erp.organization.outputs.ResultModel;
import com.kapok.erp.organization.services.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

	@PreAuthorize("hasAuthority('dictionary:query')")
    @GetMapping
    public PagedModel<Dictionary> query(Dictionary dictionary, Pageable pageable) {
        Page<Dictionary> page = dictionaryService.queryList(dictionary, pageable);
        return PagedModel.success(page);
    }

    @GetMapping("/{id}")
    public ResultModel<Dictionary> find(@PathVariable("id") Integer id) {
        Optional<Dictionary> user = dictionaryService.findBy(id);
        return ResultModel.success(user.orElseGet(null));
    }

    @PostMapping
    public ResultModel<Void> save(@RequestBody Dictionary Dictionary) {
        dictionaryService.save(Dictionary);
        return ResultModel.success(null);
    }

    @DeleteMapping("/{id}")
    public ResultModel<Void> delete(@PathVariable("id") Integer id) {
        dictionaryService.delete(id);
        return ResultModel.success(null);
    }
}
