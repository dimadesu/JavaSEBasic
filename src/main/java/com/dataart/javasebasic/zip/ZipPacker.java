package com.dataart.javasebasic.zip;

import java.io.*;
import java.util.zip.*;

import com.dataart.javasebasic.App;

public class ZipPacker {

	public static void zipFolder(File folderToZipFile) {
		
		checkFolder(folderToZipFile);
		
		try {
		
			// If there is a folder that does not have archive indicators inside, then zip it and delete folder
			String zipFileName = folderToZipFile.toString().replaceAll(App.ARCHIVE_INDICATOR + "$", "");
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
			App.logger.debug("Packing : " + folderToZipFile);
			zipDir(folderToZipFile, folderToZipFile, out);
			out.close();
			// This will force unlocking file for delete
			System.gc();
			App.logger.debug(" Created archive: " + zipFileName);
			Boolean isZipValid = isZipValid(new File(zipFileName));
			if(isZipValid) {
				App.logger.trace(" Zip archive valid.");
			} else {
				App.logger.error(" Zip archive invalid.");
			}
			App.deleteAnything(folderToZipFile);
		
		} catch (IOException e) {
			App.logger.error("IOException", e);
		}
		
	}

	public static void checkFolder(File parentFolder) {
		
		App.logger.trace("Checking folder: " + parentFolder);
		
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
				App.logger.trace(" Relative empty folder name: " + relative);
				try {
					out.putNextEntry(new ZipEntry(relative));
					out.closeEntry();
				} catch (IOException e) {
					App.logger.error("IOException", e);
				}
			} else {
				App.logger.trace(" Relative folder name: " + relative);
				try {
					out.putNextEntry(new ZipEntry(relative));
					out.closeEntry();
				} catch (IOException e) {
					App.logger.error("IOException", e);
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
				
				FileInputStream in = new FileInputStream(fileItem);
				
				App.logger.trace(" Relative file name: " + relative);
				
				out.putNextEntry(new ZipEntry(relative));
				
				int len;
				while ((len = in.read(tmpBuf)) > 0) {
					out.write(tmpBuf, 0, len);
				}
				
				App.logger.debug(" Packed: " + fileItem);

				out.closeEntry();
				in.close();
			
			} catch (IOException e) {
				App.logger.error("IOException", e);
			}
		}
		
	}
	
	static boolean isZipValid(final File file) {
	    ZipFile zipfile = null;
	    try {
	        zipfile = new ZipFile(file);
	        return true;
	    } catch (ZipException e) {
	    	App.logger.error("ZipException", e);
	        return false;
	    } catch (IOException e) {
	    	App.logger.error("IOException", e);
	        return false;
	    } finally {
	        try {
	            if (zipfile != null) {
	                zipfile.close();
	                zipfile = null;
	            }
	        } catch (IOException e) {
	        	App.logger.error("IOException", e);
	        }
	    }
	}
}
