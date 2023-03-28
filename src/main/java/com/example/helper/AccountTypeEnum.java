package com.example.helper;

public enum AccountTypeEnum {
	ACCOUNT_OFTEN("ACCOUNT_OFTEN"), ACCOUNT_GOOGLE("ACCOUNT_GOOGLE");

	private final String type;

	private AccountTypeEnum(String type) {
		this.type = type;
	}

	public String toString() {
		return this.type;
	}
}
