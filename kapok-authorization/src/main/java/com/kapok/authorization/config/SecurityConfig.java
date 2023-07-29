package com.kapok.authorization.config;

import com.kapok.authorization.constants.ApplicationConstants;
import com.kapok.authorization.support.CustomUser;
import com.kapok.authorization.support.SendResponseAndLogoutAuthenticationSuccessHandler;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private ApplicationConstants applicationConstants;

	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.authorizationEndpoint(authorizationEndpoint ->
						authorizationEndpoint.authorizationResponseHandler(
								new SendResponseAndLogoutAuthenticationSuccessHandler())
				)
				.oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
		http
				// Redirect to the login page when not authenticated from the
				// authorization endpoint
				.exceptionHandling((exceptions) -> exceptions
						.defaultAuthenticationEntryPointFor(
								new LoginUrlAuthenticationEntryPoint("/login"),
								new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
						)
				)
				// Accept access tokens for User Info and/or Client Registration
				.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
		return http.cors(Customizer.withDefaults()).build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/actuator/**", "/webjars/**", "/assets/**", "/favicon.ico", "/login").permitAll()
						.anyRequest().authenticated()
				)
				// Form login handles the redirect to the login page from the
				// authorization server filter chain
				.formLogin(formLogin ->
						formLogin.loginPage("/login")
				);
		return http.cors(Customizer.withDefaults()).build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setAllowedOrigins(applicationConstants.getCorsRedirectUris());
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
		return (context) -> {
			if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
				Authentication authentication = context.getPrincipal();
				CustomUser customUser = (CustomUser) authentication.getPrincipal();
				OidcUserInfo userInfo = OidcUserInfo.builder()
						.subject(customUser.getUsername())
						.name(customUser.getRealname())
						.nickname(customUser.getNickname())
						.picture(customUser.getPicture())
						.phoneNumber(customUser.getPhoneNumber())
						.phoneNumberVerified(customUser.getPhoneNumberVerified())
						.email(customUser.getEmail())
						.emailVerified(customUser.getEmailVerified())
						.gender(Byte.valueOf("1").equals(customUser.getGender()) ? "male" : "female")
						.birthdate(ObjectUtils.toString(customUser.getBirthdate()))
						.address(customUser.getAddress())
						.build();
				context.getClaims().claims(claims ->
						claims.putAll(userInfo.getClaims()));
			} else if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
				context.getClaims().claims((claims) -> {
					claims.put("authorities", context.getPrincipal().getAuthorities()
							.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
				});
			}
		};
	}
}
