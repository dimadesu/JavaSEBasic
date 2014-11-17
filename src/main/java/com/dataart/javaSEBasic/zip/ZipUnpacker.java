package com.dataart.javaSEBasic.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.dataart.javaSEBasic.App;
import com.dataart.javaSEBasic.file.RecursiveFileIterator;
 
public class ZipUnpacker {
	
    public String unzip(String archiveFilePath, String destDirectory, String extension, Boolean isUnzipRecursively) throws IOException {
        
        System.out.println(extension + " archive found: " + archiveFilePath);
        
        File archiveFile = new File(archiveFilePath);
        
        // Generate random string to avoid name conflicts
        UUID uuid = UUID.randomUUID();
        
        String unzippedName = archiveFile.getName() + "_" + uuid.toString();
    	
    	if(extension.contains("zip")) {
        	
    		destDirectory = destDirectory + File.separator + unzippedName;
        	File destDir = new File(destDirectory);
            if(!destDir.exists()) {
            	destDir.mkdir();            	
            }
            
            System.out.println("Unzipping into directory: " + destDir);
        	
        	ZipInputStream zipIn = new ZipInputStream(new FileInputStream(archiveFilePath));
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
        
        System.out.println("Extracted to: " + filePath);
        
        // Add to text files if extracted is not archive
        File extractedFile = new File (filePath);
        RecursiveFileIterator recurser = new RecursiveFileIterator();
        String ext = recurser.getExtension(extractedFile);
        if (!ext.contains("zip") && !ext.contains("gz")) {
        	App.textFileFound(filePath);
        }
        
    }
}
