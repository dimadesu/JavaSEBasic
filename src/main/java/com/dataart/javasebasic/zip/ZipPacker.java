package com.dataart.javasebasic.zip;

import java.io.*;
import java.util.zip.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dataart.javasebasic.App;

public class ZipPacker {
	
	private final static Logger logger = LogManager.getLogger("AppLogger");

	public static void zipFolder(File folderToZipFile) {
		
		checkFolder(folderToZipFile);
		
		try {
		
			// If there is a folder that does not have archive indicators inside, then zip it and delete folder
			String zipFileName = folderToZipFile.toString().replaceAll(App.ARCHIVE_INDICATOR + "$", "");
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
			logger.debug("Packing : " + folderToZipFile);
			zipDir(folderToZipFile, folderToZipFile, out);
			out.close();
			// This will force unlocking file for delete
			System.gc();
			logger.debug(" Created archive: " + zipFileName);
			Boolean isZipValid = isZipValid(new File(zipFileName));
			if(isZipValid) {
				logger.trace(" Zip archive valid.");
			} else {
				logger.error(" Zip archive invalid.");
			}
			deleteAnything(folderToZipFile);
		
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		
	}

	public static void checkFolder(File parentFolder) {
		
		logger.trace("Checking folder: " + parentFolder);
		
		File[] files = parentFolder.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				File dir = files[i]; 
				if(dir.getName().endsWith(App.ARCHIVE_INDICATOR)) {
					zipFolder(dir);
					// Ask to rerun itself
					checkFolder(parentFolder);
					break;
				}
				// Recursively iterate folders
				checkFolder(dir);
			}
		}
		
	}

	public static void zipDir(File zipRootDir, File targetDir, ZipOutputStream out) {

		File[] files = targetDir.listFiles();
		
		byte[] tmpBuf = new byte[1024];
		
		String relative = zipRootDir.toURI()
				.relativize(targetDir.toURI()).getPath();
		
		// Manage folders, excluding the root case
		if(!zipRootDir.equals(targetDir)) {
			
			// Manage empty folder
			if(files.length == 0) {
				logger.trace(" Relative empty folder name: " + relative);
				try {
					out.putNextEntry(new ZipEntry(relative));
					out.closeEntry();
				} catch (IOException e) {
					logger.error("IOException", e);
				}
			} else {
				logger.trace(" Relative folder name: " + relative);
				try {
					out.putNextEntry(new ZipEntry(relative));
					out.closeEntry();
				} catch (IOException e) {
					logger.error("IOException", e);
				}
			}
			
		}

		for (int i = 0; i < files.length; i++) {
			try {
			
				File fileItem = files[i];
				
				relative = zipRootDir.toURI()
						.relativize(fileItem.toURI()).getPath();
				
				if (fileItem.isDirectory()) {
					zipDir(zipRootDir, fileItem, out);
					continue;
				}
				
				if(fileItem.getName().contains(".gz" + App.ARCHIVE_INDICATOR)) {
					deleteAnything(fileItem);
					continue;
				}
				
				FileInputStream in = new FileInputStream(fileItem);
				
				logger.trace(" Relative file name: " + relative);
				
				out.putNextEntry(new ZipEntry(relative));
				
				int len;
				while ((len = in.read(tmpBuf)) > 0) {
					out.write(tmpBuf, 0, len);
				}
				
				logger.debug(" Packed: " + fileItem);

				out.closeEntry();
				in.close();
			
			} catch (IOException e) {
				logger.error("IOException", e);
			}
		}
		
	}
	
	public static boolean deleteAnything(File deleteFile) {
		Boolean isFolderDeleted = false;
		if(deleteFile.exists()){
			if(deleteFile.isDirectory()) {
				File[] files = deleteFile.listFiles();
		        if(null!=files){
		            for(int i=0; i<files.length; i++) {
		            	deleteAnything(files[i]);
		            }
		        }
		        logger.trace("Deleting folder: " + deleteFile);
			    isFolderDeleted = deleteFile.delete();
			    if(isFolderDeleted) {
			    	logger.trace("Delete success.");
			    } else {
			    	logger.error("Delete failed.");
			    	deleteAnything(deleteFile);
			    }
			} else {
				logger.trace("Deleting file: " + deleteFile);
                Boolean isFileDeleted = deleteFile.delete();
                if(isFileDeleted) {
        	    	logger.trace("Delete success.");
        	    } else {
        	    	logger.error("Delete failed.");
        	    	deleteAnything(deleteFile);
        	    }
			}
	    }
	    return isFolderDeleted;
	}
	
	static boolean isZipValid(final File file) {
	    ZipFile zipfile = null;
	    try {
	        zipfile = new ZipFile(file);
	        return true;
	    } catch (ZipException e) {
	    	logger.error("ZipException", e);
	        return false;
	    } catch (IOException e) {
	    	logger.error("IOException", e);
	        return false;
	    } finally {
	        try {
	            if (zipfile != null) {
	                zipfile.close();
	                zipfile = null;
	            }
	        } catch (IOException e) {
	        	logger.error("IOException", e);
	        }
	    }
	}
}
