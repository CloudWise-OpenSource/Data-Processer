package com.cloudwise.sdg.utils;

public class NumberUtils {
	public static boolean isInteger(String text) {
		boolean isTrue = true;
		if (text != null) {
			try {
				Integer.parseInt(text);
			} catch (NumberFormatException ex) {
				isTrue = false;
			}
		} else {
			isTrue = false;
		}
		return isTrue;
	}
}
