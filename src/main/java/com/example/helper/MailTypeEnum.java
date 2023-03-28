package com.example.helper;

public enum MailTypeEnum {
	FORGOT_PASSWORD("forgot_password"),REGISTER("register"),SHARE_VIDEO("share_video");

	private final String type;

	private MailTypeEnum(String type) {
		this.type = type;
	}

	public String toString() {
		return this.type;
	}
}
