package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.User;
import com.kapok.erp.organization.inputs.QueryParam;
import com.kapok.erp.organization.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Page<User> queryList(User user, QueryParam queryParam, Pageable pageable) {
		ExampleMatcher matcher;
		if (StringUtils.isNotBlank(queryParam.getKeyword())) {
			user.setSubject(queryParam.getKeyword());
			user.setUsername(queryParam.getKeyword());
			matcher = ExampleMatcher.matchingAny();
		} else {
			matcher = ExampleMatcher.matchingAll();
		}
		matcher = matcher.withMatcher("username", match -> match.contains())
				.withMatcher("nickname", match -> match.contains());
		Example<User> example = Example.of(user, matcher);
		Page<User> page = userRepository.findAll(example, pageable);
		page.forEach(u -> {
			u.setPassword(null);
			u.setPwdChangeDate(null);
		});
		return page;
	}

	@Override
	public Optional<User> findBy(Integer id) {
		Optional<User> user = userRepository.findById(id);
		user.ifPresent(u -> {
			u.setPassword(null);
			u.setPwdChangeDate(null);
		});
		return user;
	}

	@Transactional
	@Override
	public void save(User user) {
		if (StringUtils.isNotBlank(user.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setPwdChangeDate(LocalDate.now());
		} else if (user.getId() != null && user.getId() > 0) {
			Optional<User> existsUser = userRepository.findById(user.getId());
			existsUser.ifPresent(u -> {
				user.setPassword(u.getPassword());
				user.setPwdChangeDate(u.getPwdChangeDate());
				user.setUnlockingDate(u.getUnlockingDate());
				user.setDisabledDate(u.getDisabledDate());
			});
		}
		userRepository.save(user);
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		if (id == null || id <= 1) {
			return;
		}
		userRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void deleteAll(List<Integer> ids) {
		ids = ids.stream().filter(id -> id != null && id > 1).toList();
		userRepository.deleteAllById(ids);
	}
}
