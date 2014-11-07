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
					//System.out.println("directory:" + file.getCanonicalPath());
					System.out.println("directory:" + file.getName());
					displayDirectoryContents(file);
				} else {
					System.out.println("     file canonical path:" + file.getCanonicalPath());
					//System.out.println("     file parent:" + file.getParent());
					
					//System.out.println("  file:" + file.getName());
					
					String extension = getExtension(file.getName());
					
					if(extension.contains("zip")) {
						UnzipUtility unzipper = new UnzipUtility();
				    	UUID idOne = UUID.randomUUID();
						String destDirectory = file.getParent() + File.separator + idOne.toString();//file.getName();
						String zipFilePath = file.getCanonicalPath();
						unzipper.unzip(zipFilePath, destDirectory);
						File destDirectoryFile = new File (destDirectory);
						displayDirectoryContents(destDirectoryFile);
					} 
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getExtension (String fileName) {
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		
		return extension;
    }

}
