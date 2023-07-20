package com.kapok.authorization.services;

import com.kapok.authorization.entities.User;
import com.kapok.authorization.repositories.UserRepository;
import com.kapok.authorization.support.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findTopBySubject(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		List<String> authorities;
		if (user.getId() == 1) {
			authorities = userRepository.acquirePermCodeAll();
		} else {
			authorities = userRepository.acquirePermCodeByUserId(user.getId());
		}
		CustomUser customUser = new CustomUser(
				user.getSubject(), user.getPassword(), user.getEnabled(),
				true, true, true,
				AuthorityUtils.createAuthorityList(authorities)
		);
		customUser
				.setRealname(user.getUsername())
				.setNickname(user.getNickname())
				.setPicture(user.getPicture())
				.setPhoneNumber(user.getPhoneNumber())
				.setPhoneNumberVerified(user.getPhoneNumberVerified())
				.setEmail(user.getEmail())
				.setEmailVerified(user.getEmailVerified())
				.setGender(user.getGender())
				.setBirthdate(user.getBirthdate())
				.setAddress(user.getAddress());
		return customUser;
	}
}
