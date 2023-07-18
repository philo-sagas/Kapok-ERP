package com.kapok.erp.organization.support;

import com.kapok.erp.organization.entities.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		User user = new User();
		user.setId(1);
		user.setUsername("admin");
		return Optional.of(user.getUsername());
	}
}
