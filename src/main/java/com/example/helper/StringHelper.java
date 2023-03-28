package com.example.helper;

public class StringHelper {
	public static boolean equals(String string1, String string2) {
		if (!string1.equals(string2))
			return false;
		return true;
	}
	
	public static boolean notNull(String string) {
		if (string == null)
			return false;
		return true;
	}
}
