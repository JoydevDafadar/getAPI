package com.example.GetAPI.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.CorsBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.GetAPI.filter.JwtAuthenticationFilter;



@Configuration
public class AuthSecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain( HttpSecurity httpSecurity ) {
				
		httpSecurity
			.csrf( csrf -> csrf.disable() )
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests( authReq -> authReq
					.requestMatchers("/auth/**").permitAll()
					.anyRequest()
					.authenticated()
					)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		
		return httpSecurity.build();

	}
	
}
