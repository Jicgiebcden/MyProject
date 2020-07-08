package com.mysoap;

public class HelloWorldService {

	public String getMessage() {
		return "Hello World!";
	}

	public String getMessage(String str) {
		return "Hello World! " + str;
	}

	public String getMessage(String str1, String str2) {
		return "Hello World! " + str1 + "&" + str2;
	}
	
	public String showMessage() {
		return "This is my first soap test!";
	}
}