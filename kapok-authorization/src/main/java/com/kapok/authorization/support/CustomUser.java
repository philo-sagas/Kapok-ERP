package com.kapok.authorization.support;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Collection;

@Accessors(chain = true)
@Getter
@Setter
public class CustomUser extends User {
	private String realname;

	private String nickname;

	private String picture;

	private String phoneNumber;

	private Boolean phoneNumberVerified;

	private String email;

	private Boolean emailVerified;

	private Byte gender;

	private LocalDate birthdate;

	private String address;

	public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
}
