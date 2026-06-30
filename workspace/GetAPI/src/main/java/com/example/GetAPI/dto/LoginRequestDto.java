package com.example.GetAPI.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginRequestDto {

	@NotNull(message = "user name is required")
	private String userName;
	
	@NotNull(message = "password is required")
	private String password;
}
