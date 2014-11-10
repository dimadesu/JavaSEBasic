package com.dataart.javaSEBasic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class EmailValidator {
 
	private Pattern pattern;
	private Matcher matcher;
 
	private static final String EMAIL_PATTERN = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*.org";
 
	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	public Matcher validate(String hex) {
 
		matcher = pattern.matcher(hex);
		return matcher;
 
	}
}
