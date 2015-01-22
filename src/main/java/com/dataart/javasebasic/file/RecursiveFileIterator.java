package com.dataart.javasebasic.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dataart.javasebasic.zip.ZipPacker;
import com.dataart.javasebasic.zip.ZipUnpacker;
import com.dataart.javasebasic.App;

public class RecursiveFileIterator {
	
	private final Logger logger = LogManager.getLogger("AppLogger");
	
	public void iterateDirectoryContents(File sourceDir, String targetDirPath, Boolean isUnzip) {
		try {
			File[] files = sourceDir.listFiles();
			for (File file : files) {
				
				if (file.isDirectory()) {
					
					logger.debug("Directory: " + file.getName());
					iterateDirectoryContents(file, null, isUnzip);
					
				} else {
					
					logger.debug("File: " + file.getCanonicalPath());
					
					if (isUnzip) {

						String destDirectory = targetDirPath == null ? file.getParent() : targetDirPath;
						String extension = getExtension(file);
						String zipFilePath = file.getCanonicalPath();

						if (extension.contains("zip") || extension.contains("gz")) {
							ZipUnpacker unzipper = new ZipUnpacker();
							unzipper.unzip(zipFilePath, destDirectory, extension, true);
						} else {
							App.textFileFound(file.getCanonicalPath());
						}

					} else if(getExtension(file).contains("txt")) {
						
						// Replace phone codes
						// Read file contents. Write into copy. Delete original. Rename copy to original
						
						String COPY_INDICATOR = "_copy";
						
						File fileCopy = new File(file + COPY_INDICATOR);
						
						try
				        {
							BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		            		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileCopy));				
			            	byte[] buffer = new byte[512 * 8];
			            	int readLength;
		                    while ((readLength = bis.read(buffer)) != -1) {
		                    	logger.trace(" buffer: " + buffer);
		                    	String chunk = new String(buffer)
					                    .replace("(101)", "(401)")
					                    .replace("(202)", "(802)")
					                    .replace("(301)", "(321)");
		                    	bos.write(chunk.getBytes(), 0, readLength);
		                    }
		                    bos.close();
				            bis.close();
				        }
				        catch(FileNotFoundException e)
				        {
				        	logger.error("File was not found!", e);
				        }
				        catch(IOException e)    
				        {
				        	logger.error("No file found!", e);
				        }
						
						ZipPacker.deleteAnything(file);
						
						fileCopy.renameTo(file);
						
					}
					
				}
			}
		} catch (IOException e) {
			logger.error("IOException. Could not get file canonical path", e);
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
