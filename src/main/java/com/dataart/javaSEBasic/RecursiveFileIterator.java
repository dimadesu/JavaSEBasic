package com.dataart.javaSEBasic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecursiveFileIterator {
	
	public void iterateDirectoryContents(File sourceDir, String targetDirPath, Boolean isUnzip) {
		try {
			File[] files = sourceDir.listFiles();
			for (File file : files) {
				
				if (file.isDirectory()) {
					
					System.out.println("Directory: " + file.getName());
					iterateDirectoryContents(file, null, isUnzip);
					
				} else {
					
					System.out.println("File: " + file.getCanonicalPath());
					
					if (isUnzip) {

						String destDirectory = targetDirPath == null ? file.getParent() : targetDirPath;
						String extension = getExtension(file);
						String zipFilePath = file.getCanonicalPath();

						if (extension.contains("zip") || extension.contains("gz")) {
							UnzipUtility unzipper = new UnzipUtility();
							unzipper.unzip(zipFilePath, destDirectory, extension, true);
						} else {
							App.textFileFound(file.getCanonicalPath());
						}

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
