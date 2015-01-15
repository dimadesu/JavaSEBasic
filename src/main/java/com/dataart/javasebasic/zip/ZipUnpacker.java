package com.dataart.javasebasic.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.dataart.javasebasic.file.RecursiveFileIterator;
import com.dataart.javasebasic.App;
 
public class ZipUnpacker {
	
    public String unzip(String archiveFilePath, String destDirectory, String extension, Boolean isUnzipRecursively) {
        
        App.logger.debug(extension + " archive path requested for unzipping: " + archiveFilePath);
        
        File archiveFile = new File(archiveFilePath);
        
        String unzippedName = archiveFile.getName() + App.ARCHIVE_INDICATOR;
        
        try {
    	
	    	if(extension.contains("zip")) {
	        	
	    		destDirectory = destDirectory + File.separator + unzippedName;
	        	File destDir = new File(destDirectory);
	            if(!destDir.exists()) {
	            	destDir.mkdir();            	
	            }
	            
	            App.logger.debug("Unzipping into directory: " + destDir);
	        	
	        	ZipInputStream zipIn = null;
				try {
					zipIn = new ZipInputStream(new FileInputStream(archiveFilePath));
				} catch (FileNotFoundException e) {
					App.logger.error("Archive not found", e);
					return (App.ARCHIVE_NOT_FOUND);
				}
		        ZipEntry entry = zipIn.getNextEntry();
	        
		        // Iterate over entries in the zip file
		        while (entry != null) {
		        	String filePath = destDirectory + File.separator + entry.getName();
		            if (!entry.isDirectory()) {
		                // If the entry is a file, extract it
		                extractFile(zipIn, filePath);
		            } else {
		                // If the entry is a directory, make the directory
		                File dir = new File(filePath);
		                dir.mkdir();
		            }
		            zipIn.closeEntry();
		            entry = zipIn.getNextEntry();
		        }
		        zipIn.close();
		        
		        if(isUnzipRecursively) {
		        	// Go inside unzipped directory. Log its content. Unzip further
			        RecursiveFileIterator recurser = new RecursiveFileIterator();
			        recurser.iterateDirectoryContents(destDir, null, true);
		        }
		        
		        return destDirectory;
	        
	        } else if (extension.contains("gz")) {
	        
	        	GZIPInputStream zipIn = new GZIPInputStream(new FileInputStream(archiveFilePath));
	        	
	        	String ungrzipedFile = destDirectory + File.separator + unzippedName + ".txt";
	        	
	        	extractFile(zipIn, ungrzipedFile);
	        	
	        	return ungrzipedFile;
	        
	        }
    	
		} catch (IOException e) {
			
			App.logger.error("IOException", e);
			
		}
    	
    	return "";
    	
    }
    
    private int BUFFER_SIZE = 4096;
    
    private void extractFile(InputStream zipIn, String filePath) throws IOException {
    	
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
        
        App.logger.debug("Extracted to: " + filePath);
        
        // Add to text files if extracted is not archive
        File extractedFile = new File (filePath);
        RecursiveFileIterator recurser = new RecursiveFileIterator();
        String ext = recurser.getExtension(extractedFile);
        if (!ext.contains("zip") && !ext.contains("gz")) {
        	App.textFileFound(filePath);
        }
        
    }
}
