package com.dataart.javasebasic.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import com.dataart.javasebasic.App;
import com.dataart.javasebasic.file.TextFileReaderWriter;

public class EmailParser {
	
	public void parse(List<String> lines, String targetEmailsFilePath) {
		List<String> emails = new ArrayList<String>();
		EmailMatcher validator = new EmailMatcher();
		for (String line : lines) {
			Matcher matcher = validator.find(line);
			while (matcher.find()) {
				String email = matcher.group();
				if (!emails.contains(email)) {
					emails.add(email);
				}
			}
		}

		// Sort
		Collections.sort(emails);

		// Log
		App.logger.info("Emails collected:");
		for (String email : emails) {
			App.logger.info(email);
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
