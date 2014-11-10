package com.dataart.javaSEBasic;

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
 
/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 * @author www.codejava.net
 *
 */
public class UnzipUtility {
	
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param archiveFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String archiveFilePath, String destDirectory, String extension) throws IOException {
        
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
        
	        // iterates over entries in the zip file
	        while (entry != null) {
	        	String filePath = destDirectory + File.separator + entry.getName();
	            if (!entry.isDirectory()) {
	                // if the entry is a file, extracts it
	                extractFile(zipIn, filePath);
	            } else {
	                // if the entry is a directory, make the directory
	                File dir = new File(filePath);
	                dir.mkdir();
	            }
	            zipIn.closeEntry();
	            entry = zipIn.getNextEntry();
	        }
	        zipIn.close();
	        
	        RecursiveFileDisplay recurser = new RecursiveFileDisplay();
	        recurser.displayDirectoryContents(destDir);
        
        } else if (extension.contains("gz")) {
        
        	GZIPInputStream zipIn = new GZIPInputStream(new FileInputStream(archiveFilePath));
        	
        	String ungrzipedFile = destDirectory + File.separator + unzippedName + ".txt";
        	
        	extractFile(zipIn, ungrzipedFile);
        
        }
    }
    
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(InputStream zipIn, String filePath) throws IOException {
    	System.out.println("Extracted to: " + filePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
        
        // Add to text files if extracted is not archive
        File extractedFile = new File (filePath);
        RecursiveFileDisplay recurser = new RecursiveFileDisplay();
        String ext = recurser.getExtension(extractedFile);
        if (!ext.contains("zip") && !ext.contains("gz")) {
        	App.textFileFound(filePath);
        }
    }
}
