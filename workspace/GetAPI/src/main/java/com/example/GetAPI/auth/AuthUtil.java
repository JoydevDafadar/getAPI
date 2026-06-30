package com.example.GetAPI.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.GetAPI.dao.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;



@Service
public class AuthUtil {
	

	@Value("${security.jwt.secretkey}")
	private String secretKey;
	
	private SecretKey getSecratKey() {
		return Keys.hmacShaKeyFor(this.secretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateAccessToken( User user ) {
		
		return Jwts.builder()
				.subject(user.getUsername())
				.claim("userId", user.getUserId().toString())
				.claim("userRole", user.getUserRole())
				.claim("userEmail", user.getUserEmail())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000*60*10 ))
				.signWith(getSecratKey())
				.compact();		
		
	}
	
	
	public String getUsernameFromToken( String token ) {
		
		Claims claims = Jwts.parser()
				.verifyWith(getSecratKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.getSubject();
	}
	
	
	
	
	

}
