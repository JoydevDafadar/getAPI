package com.example.GetAPI.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.GetAPI.auth.AuthUtil;
import com.example.GetAPI.dao.User;
import com.example.GetAPI.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(3)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserRepository userRepository;
	
	private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Autowired
	private AuthUtil authUtil;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		final String headerToken = request.getHeader("Authorization");
		log.debug("headerToke : " + headerToken);
		
		if( headerToken == null || !headerToken.startsWith("Bearer ") ) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = headerToken.split("Bearer ")[1];
		String userName = authUtil.getUsernameFromToken(token);
		
		if( userName != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			
			// Finding user object 
			User user = this.userRepository.findByUserNameAndUsergroupId(userName, 11111L).orElseThrow();
			
			UsernamePasswordAuthenticationToken authenticationToken 
											= new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
		}
		
		
		filterChain.doFilter(request, response);
		
	}

}
