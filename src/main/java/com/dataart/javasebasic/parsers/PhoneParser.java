package com.dataart.javasebasic.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dataart.javasebasic.file.TextFileReaderWriter;

public class PhoneParser {
	
	private final Logger logger = LogManager.getLogger("AppLogger");
	
	public void parse (List<String> lines, String targetPhonesFilePath) {

		List<String> phones = new ArrayList<String>();

		// Strip phones to digits only format
		for (String line : lines) {
			// Everything before @ is phone plus email name
			Integer atSignIndex = line.indexOf("@");

			if (atSignIndex == -1) {
				continue;
			}

			String phoneEmail = line.substring(0, atSignIndex);

			// Phone without email part
			Integer phoneEndIndex = phoneEmail.lastIndexOf(" ");

			if (phoneEndIndex == -1) {
				continue;
			}

			String phoneDirty = line.substring(0, phoneEndIndex);

			// Collect digits
			char[] phoneCharArray = phoneDirty.toCharArray();
			List<Character> digitsList = new ArrayList<Character>();

			for (Character ch : phoneCharArray) {
				if (Character.isDigit(ch)) {
					digitsList.add(ch);
				}
			}

			// ArrayList to String
			StringBuilder builder = new StringBuilder(digitsList.size());
			for (Character ch : digitsList) {
				builder.append(ch);
			}

			phones.add(builder.toString());
		}

		// Replace codes
		List<String> phonesReplaced = new ArrayList<String>();
		for (String phone : phones) {

			// Skip too short numbers
			if (phone.length() <= 5) {
				continue;
			}

			// Change phone to format in the task
			phone = "+" + phone.charAt(0) + " (" + phone.substring(1, 4) + ") "
					+ phone.substring(5);
			phonesReplaced.add(phone);
		}

		// Sort
		Collections.sort(phonesReplaced);
		logger.info("Phones collected:");

		// Log results to console
		for (String phone : phonesReplaced) {
			logger.info(phone);
		}

		// Save results to a file
		TextFileReaderWriter readerWriter = new TextFileReaderWriter();
		readerWriter.writeLargerTextFile(targetPhonesFilePath, phonesReplaced);

	}

}
