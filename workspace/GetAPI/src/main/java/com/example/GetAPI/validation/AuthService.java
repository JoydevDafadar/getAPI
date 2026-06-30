package com.example.GetAPI.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.GetAPI.auth.AuthUtil;
import com.example.GetAPI.dao.User;
import com.example.GetAPI.dto.LoginRequestDto;
import com.example.GetAPI.dto.LoginResponseDto;
import com.example.GetAPI.dto.SignUpRequestDto;
import com.example.GetAPI.repository.UserRepository;

@Service
public class AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public LoginResponseDto	loginUser(LoginRequestDto loginRequestDto) {
		
		Authentication authentication =  authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDto.getUserName(), loginRequestDto.getPassword())
				);
		
		User user = (User) authentication.getPrincipal();
		String tokenString = authUtil.generateAccessToken(user);
		
		return new LoginResponseDto( user.getUserId(), user.getUsername(), tokenString );
		
	}

	
	public LoginResponseDto signupUser(SignUpRequestDto signUpRequestDto) {
		
		User user = userRepository.findByUserNameAndUsergroupId( signUpRequestDto.getUserName(), 12345L ).orElse(null);
		
		if( user == null ) {
			
			Long userIdLong = this.userRepository.generateTblSequence();
			
			User createdUser = User.builder()
					.userName(signUpRequestDto.getUserName())
					.userId(userIdLong)
					.usergroupId(12345L)
					.password(passwordEncoder.encode(signUpRequestDto.getPassword()))
					.userRole(signUpRequestDto.getUserRole())
					.userEmail(signUpRequestDto.getUserEmail())
					.build();
			
			userRepository.save(createdUser);
			
			String tokenString = authUtil.generateAccessToken(createdUser);
			
			return LoginResponseDto.builder()
					.userId(createdUser.getUserId())
					.userName(createdUser.getUsername())
					.token(tokenString)
					.build();
			
		}else {
			throw new IllegalArgumentException("User Name already exists in the SYSTEM");
		}
		
	}

}
