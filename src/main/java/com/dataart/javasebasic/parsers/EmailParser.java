package com.dataart.javasebasic.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dataart.javasebasic.file.TextFileReaderWriter;

public class EmailParser {
	
	private final Logger logger = LogManager.getLogger("AppLogger");
	
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
		logger.info("Emails collected:");
		for (String email : emails) {
			logger.info(email);
		}

		// Write to file
		TextFileReaderWriter readerWriter = new TextFileReaderWriter();
		readerWriter.writeLargerTextFile(targetEmailsFilePath, emails);
	}

}
