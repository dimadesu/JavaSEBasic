package com.dataart.javaSEBasic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipInputStream;

public class RecursiveFileDisplay {

	public void displayDirectoryContents(File dir) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.println("Directory: " + file.getName());
					displayDirectoryContents(file);
				} else {
					System.out.println("File: " + file.getCanonicalPath());
					
					String extension = getExtension(file);
					String destDirectory = file.getParent();
					String zipFilePath = file.getCanonicalPath();
					
					if(extension.contains("zip") || extension.contains("gz")) {
						UnzipUtility unzipper = new UnzipUtility();
						unzipper.unzip(zipFilePath, destDirectory, extension);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getExtension (File file) {
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
