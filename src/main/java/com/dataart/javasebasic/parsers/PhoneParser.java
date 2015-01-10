package com.dataart.javasebasic.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dataart.javasebasic.file.TextFileReaderWriter;

public class PhoneParser {
	
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

			String code = phone.substring(1, 4);
			String replacement = null;

			if (code.equals("101")) {
				replacement = "401";
			} else if (code.equals("202")) {
				replacement = "802";
			} else if (code.equals("301")) {
				replacement = "321";
			}

			if (replacement != null) {
				phone = phone.charAt(0) + replacement + phone.substring(4);
			}

			// Change phone to format in the task
			phone = "+" + phone.charAt(0) + " (" + phone.substring(1, 5) + ") "
					+ phone.substring(6);
			phonesReplaced.add(phone);
		}

		// Sort
		java.util.Collections.sort(phonesReplaced);
		System.out.println("Phones collected:");

		// Log results to console
		for (String phone : phonesReplaced) {
			System.out.println(phone);
		}

		// Save results to a file
		TextFileReaderWriter readerWriter = new TextFileReaderWriter();
		try {
			readerWriter.writeLargerTextFile(targetPhonesFilePath,
					phonesReplaced);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
