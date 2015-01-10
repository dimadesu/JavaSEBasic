package com.dataart.javasebasic.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class EmailMatcher {
 
	private Pattern pattern;
	private Matcher matcher;
 
	private static final String EMAIL_PATTERN = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*.org";
 
	public EmailMatcher() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	public Matcher find(String hex) {
 
		matcher = pattern.matcher(hex);
		return matcher;
 
	}
}
