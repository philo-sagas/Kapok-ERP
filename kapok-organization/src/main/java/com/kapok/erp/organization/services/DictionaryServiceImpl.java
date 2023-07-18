package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.Dictionary;
import com.kapok.erp.organization.repositories.DictionaryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public Page<Dictionary> queryList(Dictionary dictionary, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());
        Example<Dictionary> example = Example.of(dictionary, matcher);
        Page<Dictionary> page = dictionaryRepository.findAll(example, pageable);
        return page;
    }

    @Override
    public Optional<Dictionary> findBy(Integer id) {
        return dictionaryRepository.findById(id);
    }

    @Override
    public void save(Dictionary dictionary) {
        dictionaryRepository.save(dictionary);
    }

    @Override
    public void delete(Integer id) {
        dictionaryRepository.deleteById(id);
    }
}
