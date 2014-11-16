package com.dataart.javaSEBasic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipPacker {
	
	ZipOutputStream zos = null;
	
	public ZipPacker (String outputFilePath) throws FileNotFoundException {

		FileOutputStream fos = new FileOutputStream(outputFilePath);
		zos = new ZipOutputStream(fos);

	}
	
	public void writeOneItem (String targetfileName, String sourceFilePath) throws IOException {
		
		// This can be moved into method.
		// arg1 path in zip file
		// arg2 path to read from
		
		byte[] buffer = new byte[1024];
		
		ZipEntry zePhones = new ZipEntry(targetfileName);
		zos.putNextEntry(zePhones);
		
		FileInputStream in = new FileInputStream(sourceFilePath);

		int len;
		while ((len = in.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}

		in.close();
		zos.closeEntry();
		
		System.out.println("Packed: " + targetfileName);
		
	}
	
	public void finishPacking () throws IOException {
		
		zos.close();
		 
		System.out.println("Finished packing.");
		
	}

}
