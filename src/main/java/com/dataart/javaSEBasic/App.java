package com.dataart.javaSEBasic;

import java.io.File;

public class App 
{
    public static void main( String[] args )
    {
    	String startDirPath = "D:/Users/dantonov/Docs/Work/000 Java Training/01 Java SE course/99 try/";
    	
    	File startDirFile = new File(startDirPath);
    	
    	RecursiveFileDisplay recurser = new RecursiveFileDisplay();
    	
    	recurser.displayDirectoryContents(startDirFile);
		
		/*String zipFilePath = "D:/Users/dantonov/Docs/Work/000 Java Training/01 Java SE course/99 try/";
    	String destDirectory = "D:/Users/dantonov/Docs/Work/000 Java Training/01 Java SE course/99 try/";*/
    	
    	/*UnzipUtility unzipper = new UnzipUtility();
        
        try {
        	
        	unzipper.unzip(zipFilePath, destDirectory);
        	
        } catch (Exception ex) {
            // some errors occurred
            ex.printStackTrace();
        }*/
        
    }
}
