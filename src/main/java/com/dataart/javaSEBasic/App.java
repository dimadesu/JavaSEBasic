package com.dataart.javaSEBasic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class App {
	
	public static void main(String[] args) {

		try {
			// Path to the folder jar is executed from
			String jarPath = App.class.getProtectionDomain().getCodeSource()
					.getLocation().getPath();
			String jarDecodedPath = null;
			String jarFolder = null;
			try {
				jarDecodedPath = URLDecoder.decode(jarPath, "UTF-8");
				File jarFile = new File(jarDecodedPath);
				jarFolder = jarFile.getParent() + "/";
				System.out.println(jarFolder);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			// Input. Output
			String inputFileName = "inputs.zip";
			String outputFileName = "inputsv2.zip";
			String inputFilePath = jarFolder + inputFileName;
			String outputFilePath = jarFolder + outputFileName;
			
			// Generate random string to avoid name conflicts
			UUID uuid = UUID.randomUUID();
			String tempFolderName = "javaSEBasic_" + uuid.toString();
			Path tempFolderPath = Files.createTempDirectory(tempFolderName);
			File tempFolderFile = tempFolderPath.toFile();
			String tempFolderString = tempFolderPath.toString();
			System.out.println("Temp folder created: " + tempFolderString);
			
			// Target paths for parsed file
			String targetDirPath = tempFolderString;
			String targetFileName = "phones.txt_emails.txt";
			String targetPhonesFileName = "phones.txt";
			String targetEmailsFileName = "emails.txt";
			String targetTextFilePath = targetDirPath + targetFileName;
			String targetPhonesFilePath = targetDirPath + targetPhonesFileName;
			String targetEmailsFilePath = targetDirPath + targetEmailsFileName;

			// Unarchive recursively. Collect text files
			UnzipUtility unzipper = new UnzipUtility();
			unzipper.unzip(inputFilePath, tempFolderString, "zip");

			// Log all the text files found in the end
			TextFileReaderWriter readerWriter = new TextFileReaderWriter();
			List<String> lines = new ArrayList<String>();
			for (String listItem : App.textFiles) {
				System.out.println(listItem);
				readerWriter.readLargerTextFile(listItem, lines);
			}
			readerWriter.writeLargerTextFile(targetTextFilePath, lines);

			// Parse
			PhoneParser phoneParser = new PhoneParser ();
			phoneParser.parse(lines, targetPhonesFilePath);
			
			EmailParser emailParser = new EmailParser ();
			emailParser.parse(lines, targetEmailsFilePath);
			
			// Zip it
			ZipPacker packer = new ZipPacker(outputFilePath);
			packer.writeOneItem(targetPhonesFileName, targetPhonesFilePath);
			packer.writeOneItem(targetEmailsFileName, targetEmailsFilePath);
			packer.finishPacking();
			
			Boolean isTempFolderDeleted = tempFolderFile.delete();
			System.out.println("Temp folder is deleted: " + (isTempFolderDeleted ? "yes" : "no" )); 

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static int textFilesCounter = 0;
	public static List<String> textFiles = new ArrayList<String>();

	public static void textFileFound(String path) {
		App.textFilesCounter++;
		App.textFiles.add(path);
		System.out.println("Text file found. Total text files so far: "
				+ textFilesCounter);
	}

}
