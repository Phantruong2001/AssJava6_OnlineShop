package com.example.util;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassword {
	private String sendMailId;

	private String email;

	private String code;

	@Size(min = 8, max = 50,message = "{Validator.password}")
	private String newPassword;

	@Size(min = 8, max = 50,message = "{Validator.password}")
	private String confimPassword;
}
