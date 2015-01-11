package com.dataart.javasebasic.zip;

import java.io.*;
import java.util.zip.*;

import com.dataart.javasebasic.App;

public class ZipPacker {

	private static String zipBaseFolder = null; 
	
	public static void zipDir(String zipFileName, String dir) throws Exception {
		File dirObj = new File(dir);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		App.logger.debug("Packing : " + zipFileName);
		zipBaseFolder = dir;
		addDir(dirObj, out);
		out.close();
	}

	public static void addDir(File dirObj, ZipOutputStream out) throws IOException {
		File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDir(files[i], out);
				continue;
			}
			String path = files[i].getAbsolutePath();
			FileInputStream in = new FileInputStream(path);
			
			String relative = new File(zipBaseFolder).toURI().relativize(new File(path).toURI()).getPath();
			
			out.putNextEntry(new ZipEntry(relative));
			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();
			App.logger.debug(" Packed: " + path);
		}
	}
}
