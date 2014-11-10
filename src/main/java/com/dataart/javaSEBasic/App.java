package com.dataart.javaSEBasic;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;

public class App 
{
    public static void main( String[] args )
    {
    	
    	// Path to the folder jar is executed from
    	String jarPath = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    	String jarDecodedPath = null;
    	String jarFolder = null;
    	try {
			jarDecodedPath = URLDecoder.decode(jarPath, "UTF-8");
			File jarFile = new File (jarDecodedPath);
			jarFolder = jarFile.getParent() + "/";
			System.out.println(jarFolder);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
    	String inputFolder = "input/";
    	String tempFolder = "temp/";
    	String outputFolder = "output/";
    	String startDirPath = jarFolder + inputFolder;
    	String targetDirPath = jarFolder + tempFolder;
    	String targetFileName = "phones.txt_emails.txt";
    	String targetPhonesFileName = "phones.txt";
    	String targetEmailsFileName = "emails.txt";
    	String targetTextFilePath = targetDirPath + targetFileName;
    	String targetPhonesFilePath = targetDirPath + targetPhonesFileName;
    	String targetEmailsFilePath = targetDirPath + targetEmailsFileName;
    	
    	File startDirFile = new File(startDirPath);
    	
    	RecursiveFileDisplay recurser = new RecursiveFileDisplay();
    	
    	// Unarchive recursively. Collect text files
    	recurser.displayDirectoryContents(startDirFile, targetDirPath);
    	
    	ReadWriteTextFileJDK7 readerWriter = new ReadWriteTextFileJDK7();
    	List<String> lines = new ArrayList<String>();

		try {
	    	
	    	// Print all the text file found in the end
	    	for(String listItem : App.textFiles) {
	    	    
	    		System.out.println(listItem);
	    		
	    		readerWriter.readLargerTextFile(listItem, lines);
	    		
	    	}
	    	
			readerWriter.writeLargerTextFile(targetTextFilePath, lines);
			
			App app = new App ();
			
			app.phoneParser(lines, targetPhonesFilePath);
			app.emailParser(lines, targetEmailsFilePath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
    
    public static int textFilesCounter = 0;
    public static List<String> textFiles = new ArrayList<String>();
    
    public static void textFileFound (String path) {
    	App.textFilesCounter++;
    	App.textFiles.add(path);
    	System.out.println("Text file found. Total text files so far: " + textFilesCounter); 
    }
    
    private void phoneParser (List<String> lines, String targetPhonesFilePath) {
    	
    	List<String> phones = new ArrayList<String>();
    	
    	// Strip phones to digits only format 
    	for(String line: lines)
    	{
    		// Everything before @ is phone plus email name
    		Integer atSignIndex = line.indexOf("@");
    		
    		if(atSignIndex == -1) {
    			continue;
    		}
    		
    		String phoneEmail = line.substring(0, atSignIndex);
    		
    		// Phone without email part
    		Integer phoneEndIndex = phoneEmail.lastIndexOf(" ");
    		
    		if(phoneEndIndex == -1) {
    			continue;
    		}
    		
    		String phoneDirty = line.substring(0, phoneEndIndex);
    		
    		// Collect digits 
    		char[] phoneCharArray = phoneDirty.toCharArray();
    		List <Character> digitsList =  new ArrayList<Character>();
    		
    		for(Character ch: phoneCharArray)
    		{
    			if (Character.isDigit(ch)) {
    				digitsList.add(ch);
    			}
    		}
    		
		    // ArrayList to String 
    		StringBuilder builder = new StringBuilder(digitsList.size());
		    for(Character ch: digitsList)
		    {
		        builder.append(ch);
		    }
    		
    		phones.add(builder.toString());
    	}
    	
    	// Replace codes
    	List<String> phonesReplaced = new ArrayList<String>();
    	for (String phone: phones) {
    		
    		// Skip too short numbers
    		if(phone.length() <= 5) {
				continue;    			
    		}
    		
    		String code = phone.substring(1,4);
    		String replacement = null;
    		
    		if(code.equals("101")) {
    			replacement = "401";
    		} else if(code.equals("202")) {
    			replacement = "802";
    		} else if(code.equals("301")) {
    			replacement = "321";
    		}
    		
    		if(replacement != null) {
    			phone = phone.charAt(0) + replacement + phone.substring(4);
    		}
    		
    		// Change phone to format in the task
    		phone = "+" + phone.charAt(0) + " (" + phone.substring(1, 5) + ") " + phone.substring(6);
    		phonesReplaced.add(phone);
    	}
    	
    	// Sort
    	java.util.Collections.sort(phonesReplaced);
    	System.out.println("Phones collected:");
    	
    	// Log results to console
    	for(String phone: phonesReplaced)
		{
    		System.out.println(phone);
		}
    	
    	// Save results to a file
    	ReadWriteTextFileJDK7 readerWriter = new ReadWriteTextFileJDK7();
    	try {
			readerWriter.writeLargerTextFile(targetPhonesFilePath, phonesReplaced);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    private void emailParser (List<String> lines, String targetEmailsFilePath) {
    	List<String> emails = new ArrayList<String>();
    	EmailValidator validator = new EmailValidator();
    	for(String line: lines)
    	{
    		Matcher matcher = validator.validate(line);
    		while(matcher.find())
			{
    			String email = matcher.group();
    			if(!emails.contains(email)){
    				emails.add(email);
    			}
			}
    	}
    	
    	// Sort
    	java.util.Collections.sort(emails);
    	
    	// Log
    	System.out.println("Emails collected:");
    	for(String email: emails)
		{
    		System.out.println(email);
		}
    	
    	// Write to file
    	ReadWriteTextFileJDK7 readerWriter = new ReadWriteTextFileJDK7();
    	try {
			readerWriter.writeLargerTextFile(targetEmailsFilePath, emails);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
