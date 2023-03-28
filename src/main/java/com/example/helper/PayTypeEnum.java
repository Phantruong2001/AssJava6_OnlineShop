package com.example.helper;

public enum PayTypeEnum {
	PAID("paid"),UNPAID("unPaid");

	private final String type;

	private PayTypeEnum(String type) {
		this.type = type;
	}

	public String toString() {
		return this.type;
	}
}
