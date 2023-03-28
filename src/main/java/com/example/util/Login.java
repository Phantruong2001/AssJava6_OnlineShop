package com.example.util;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
	@NotEmpty(message = "{NotEmpty.User.email}")
	@Email(message = "{Email.User.email}")
	private String email;

	private String password;
	
	private boolean rememberme = false;
}
