package com.dataart.javaSEBasic;

import java.io.File;
import java.io.IOException;

public class RecursiveFileDisplay {

	public void displayDirectoryContents(File sourceDir, String targetDirPath) {
		try {
			File[] files = sourceDir.listFiles();
			for (File file : files) {
				String destDirectory = targetDirPath == null ? file.getParent() : targetDirPath;
				if (file.isDirectory()) {
					System.out.println("Directory: " + file.getName());
					displayDirectoryContents(file, null);
				} else {
					System.out.println("File: " + file.getCanonicalPath());
					
					String extension = getExtension(file);
					String zipFilePath = file.getCanonicalPath();
					
					if(extension.contains("zip") || extension.contains("gz")) {
						UnzipUtility unzipper = new UnzipUtility();
						unzipper.unzip(zipFilePath, destDirectory, extension);
					} else {
						App.textFileFound(file.getCanonicalPath());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getExtension (File file) {
		String extension = "";
		
		if(!file.isDirectory()) {

			String fileName = file.getName();
			
			int i = fileName.lastIndexOf('.');
			
			if (i > 0) {
			    extension = fileName.substring(i+1);
			}
			
		}
		
		return extension;
    }

}
