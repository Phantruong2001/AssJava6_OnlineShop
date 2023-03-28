package com.example.helper;

public enum StatusTypeEnum {
	DELIVERED("delivered"), BEING_TRANSPORTED("being_transported"), WAITING("waiting");

	private final String type;

	private StatusTypeEnum(String type) {
		this.type = type;
	}

	public String toString() {
		return this.type;
	}
}
