package com.kapok.authorization.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout() {
		return "logout";
	}

	@ResponseBody
	@GetMapping("/")
	public Map<String, Object> index(
			Principal principal,
			Authentication authentication,
			@AuthenticationPrincipal Object authenticationPrincipal) {
		Map<String, Object> map = new HashMap<>();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		map.put("principal", principal);
		map.put("authentication", authentication);
		map.put("securityContext", securityContext);
		map.put("authenticationPrincipal", authenticationPrincipal);
		map.put("authenticationPrincipal.class", authenticationPrincipal.getClass());
		return map;
	}
}
