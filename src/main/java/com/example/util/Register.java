package com.example.util;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.validator.BirthDay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {

	@NotEmpty(message = "{NotEmpty.User.email}")
	@Email(message = "{Email.User.email}")
	private String email;

	@NotEmpty(message = "{NotEmpty.User.fullName}")
	@Size(min = 5, message = "{Size.User.fullName}")
	private String fullName;

	@NotEmpty(message = "{NotEmpty.User.address}")
	@Size(min = 10, message = "{Size.User.address}")
	private String address;

	@BirthDay
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDay;
}
