package com.example.GetAPI.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GetAPI.auth.CustomUserDetailsService;
import com.example.GetAPI.dao.User;
import com.example.GetAPI.dto.LoginRequestDto;
import com.example.GetAPI.dto.LoginResponseDto;
import com.example.GetAPI.dto.SignUpRequestDto;
import com.example.GetAPI.validation.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private AuthService authService;
	
	private final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	
	@PostMapping("/login")
	public ResponseEntity<Object> loginUser( 
			@RequestBody LoginRequestDto loginRequestDto
			) {
		
		try {
			LoginResponseDto loginResponseDto = this.authService.loginUser(loginRequestDto);
			return ResponseEntity.status(200).body(loginResponseDto);
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}
		
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<Object> signupUser(
			@RequestBody SignUpRequestDto signUpRequestDto
			) {
		
		try {
			LoginResponseDto loginResponseDto = this.authService.signupUser(signUpRequestDto);
			return ResponseEntity.status(200).body(loginResponseDto);
		} catch (Exception e) {
			log.debug("Exception : /save :- " + e);
			return ResponseEntity.status(404).body(Map.of("cause", e.getMessage()));
		}
		
	}
	

}
