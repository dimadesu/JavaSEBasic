package com.dataart.javaSEBasic;

import java.io.File;
import java.util.*;

public class App 
{
    public static void main( String[] args )
    {
    	
    	// TODO: Hardcoded path
    	String startDirPath = "D:/Users/dantonov/Docs/Work/000 Java Training/01 Java SE course/99 try/";
    	
    	File startDirFile = new File(startDirPath);
    	
    	RecursiveFileDisplay recurser = new RecursiveFileDisplay();
    	
    	recurser.displayDirectoryContents(startDirFile);
    	
    	// Print all the text file found in the end
    	for(String listItem : App.textFiles) {
    	    System.out.println(listItem);
    	}
        
    }
    
    public static int textFilesCounter = 0;
    public static List<String> textFiles = new ArrayList<String>();
    
    public static void textFileFound (String path) {
    	App.textFilesCounter++;
    	App.textFiles.add(path);
    	System.out.println("Text file found. Total text files so far: " + textFilesCounter); 
    }
    
}
