package com.dataart.javaSEBasic;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

public class App 
{
    public static void main( String[] args )
    {
    	
    	// TODO: Hardcoded path
    	String startDirPath = "D:/Users/dantonov/Docs/Work/000 Java Training/01 Java SE course/99 try/";
    	String targetFileName = "phones.txt_emails.txt";
    	String targetPhonesFileName = "phones.txt";
    	String targetEmailsFileName = "emails.txt";
    	String targetTextFilePath = startDirPath + targetFileName;
    	String targetPhonesFilePath = startDirPath + targetPhonesFileName;
    	String targetEmailsFilePath = startDirPath + targetEmailsFileName;
    	
    	File startDirFile = new File(startDirPath);
    	
    	RecursiveFileDisplay recurser = new RecursiveFileDisplay();
    	
    	// Unarchive recursively. Collect text files
    	recurser.displayDirectoryContents(startDirFile);
    	
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
    	System.out.println("Phones collected:");
    	for(String phone: phones)
		{
    		System.out.println(phone);
		}
    	ReadWriteTextFileJDK7 readerWriter = new ReadWriteTextFileJDK7();
    	try {
			readerWriter.writeLargerTextFile(targetPhonesFilePath, phones);
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
    	System.out.println("Emails collected:");
    	for(String email: emails)
		{
    		System.out.println(email);
		}
    	ReadWriteTextFileJDK7 readerWriter = new ReadWriteTextFileJDK7();
    	try {
			readerWriter.writeLargerTextFile(targetEmailsFilePath, emails);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
