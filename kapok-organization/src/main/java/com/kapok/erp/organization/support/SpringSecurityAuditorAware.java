package com.kapok.erp.organization.support;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<String> subject = Optional.ofNullable(authentication)
				.filter(Authentication::isAuthenticated)
				.map(Authentication::getPrincipal)
				.map(Jwt.class::cast)
				.map(Jwt::getSubject);
		return subject;
	}
}
