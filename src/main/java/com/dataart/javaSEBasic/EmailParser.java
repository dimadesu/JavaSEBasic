package com.dataart.javaSEBasic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class EmailParser {
	
	public void parse(List<String> lines, String targetEmailsFilePath) {
		List<String> emails = new ArrayList<String>();
		EmailValidator validator = new EmailValidator();
		for (String line : lines) {
			Matcher matcher = validator.validate(line);
			while (matcher.find()) {
				String email = matcher.group();
				if (!emails.contains(email)) {
					emails.add(email);
				}
			}
		}

		// Sort
		java.util.Collections.sort(emails);

		// Log
		System.out.println("Emails collected:");
		for (String email : emails) {
			System.out.println(email);
		}

		// Write to file
		TextFileReaderWriter readerWriter = new TextFileReaderWriter();
		try {
			readerWriter.writeLargerTextFile(targetEmailsFilePath, emails);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
