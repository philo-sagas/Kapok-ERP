package com.kapok.authorization.support;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SendResponseAndLogoutAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
				(OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(authorizationCodeRequestAuthentication.getRedirectUri())
				.queryParam(OAuth2ParameterNames.CODE, authorizationCodeRequestAuthentication.getAuthorizationCode().getTokenValue());
		if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
			uriBuilder.queryParam(
					OAuth2ParameterNames.STATE,
					UriUtils.encode(authorizationCodeRequestAuthentication.getState(), StandardCharsets.UTF_8));
		}
		String redirectUri = uriBuilder.build(true).toUriString();        // build(true) -> Components are explicitly encoded
		this.redirectStrategy.sendRedirect(request, response, redirectUri);

		SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
		SecurityContext context = securityContextHolderStrategy.getContext();
		securityContextHolderStrategy.clearContext();
		if (context != null) {
			context.setAuthentication(null);
		}
		SecurityContext emptyContext = securityContextHolderStrategy.createEmptyContext();
		securityContextRepository.saveContext(emptyContext, request, response);
	}
}
