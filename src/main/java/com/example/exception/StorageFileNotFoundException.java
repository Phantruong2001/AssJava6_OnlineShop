package com.example.exception;

import java.net.MalformedURLException;

public class StorageFileNotFoundException extends StorageException {

	public StorageFileNotFoundException(String message, MalformedURLException e) {
		super(message);
	}

	public StorageFileNotFoundException(String message) {
		super(message);
	}
}
