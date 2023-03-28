package com.example.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BirthDayValidator implements ConstraintValidator<BirthDay, Date> {

	@Override
	public boolean isValid(Date value, ConstraintValidatorContext context) {
		if (value == null)
			return false;
		Date dateNow = new Date();
		int yearNow = dateNow.getYear();
		int yearValue = value.getYear();
		if (yearNow - yearValue < 18 || yearNow - yearValue > 45)
			return false;
		return true;
	}
}
