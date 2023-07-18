package com.kapok.erp.organization.services;

import com.kapok.erp.organization.entities.User;
import com.kapok.erp.organization.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> queryList(User user, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username", match -> match.startsWith());
        Example<User> example = Example.of(user, matcher);
        Page<User> page = userRepository.findAll(example, pageable);
        return page;
    }

    @Override
    public Optional<User> findBy(Integer id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> {
            u.setPassword(null);
            u.setSalt(null);
            u.setPwdChangeDate(null);
        });
        return user;
    }

    @Override
    public void save(User user) {
        if (StringUtils.isNotBlank(user.getPassword())) {
            SecureRandom secureRandom = new SecureRandom();
            byte[] bytes = new byte[9];
            secureRandom.nextBytes(bytes);
            String saltBytes = new String(Base64.getEncoder().encode(bytes));
            String salt = "$6$" + saltBytes;
            String hashedPassword = Crypt.crypt(user.getPassword(), salt);
            user.setPassword(hashedPassword);
            user.setSalt(salt);
            user.setPwdChangeDate(LocalDate.now());
        } else if (user.getId() != null && user.getId() > 0) {
            Optional<User> existsUser = userRepository.findById(user.getId());
            existsUser.ifPresent(u -> {
                user.setPassword(u.getPassword());
                user.setSalt(u.getSalt());
                user.setPwdChangeDate(u.getPwdChangeDate());
            });
        }
        userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
