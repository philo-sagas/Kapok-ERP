package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Dictionary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DictionaryService {
    Page<Dictionary> queryList(Dictionary dictionary, Pageable pageable);

    Optional<Dictionary> findBy(Integer id);

    void save(Dictionary dictionary);

    void delete(Integer id);
}
