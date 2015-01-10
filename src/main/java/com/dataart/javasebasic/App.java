package com.dataart.javasebasic;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.dataart.javasebasic.file.TextFileReaderWriter;
import com.dataart.javasebasic.parsers.EmailParser;
import com.dataart.javasebasic.parsers.PhoneParser;
import com.dataart.javasebasic.zip.ZipPacker;
import com.dataart.javasebasic.zip.ZipUnpacker;

public class App {
	
	private static final Logger logger = LogManager.getLogger("AppLogger");
	
	public static void main(String[] args) {

		try {
			
			logger.trace("Entering application.");
			
			System.out.println("Press Enter key to continue");
			System.in.read();
			
			// Path to the folder jar is executed from
			String jarPath = App.class.getProtectionDomain().getCodeSource()
					.getLocation().getPath();
			String jarDecodedPath = null;
			String jarFolder = null;
			try {
				jarDecodedPath = URLDecoder.decode(jarPath, "UTF-8");
				File jarFile = new File(jarDecodedPath);
				jarFolder = jarFile.getParent() + "/";
				System.out.println("jarFolder: " + jarFolder);
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
			String tempFolderName = "javasebasic_" + uuid.toString();
			Path tempFolderPath = Files.createTempDirectory(tempFolderName);
			File tempFolderFile = tempFolderPath.toFile();
			String tempFolderString = tempFolderPath.toString();
			System.out.println("Temp folder created: " + tempFolderString);
			
			// Target paths for parsed file
			String targetFileName = "phones.txt_emails.txt";
			String targetPhonesFileName = "phones.txt";
			String targetEmailsFileName = "emails.txt";
			String targetTextFilePath = tempFolderString + File.separator + targetFileName;

			// Unarchive recursively. Collect text files
			ZipUnpacker unzipper = new ZipUnpacker();
			unzipper.unzip(inputFilePath, tempFolderString, "zip", true);
			
			// Pause
			System.out.println("Press Enter key to continue");
			System.in.read();

			// Log all the text files found in the end
			TextFileReaderWriter readerWriter = new TextFileReaderWriter();
			List<String> lines = new ArrayList<String>();
			for (String listItem : App.textFiles) {
				System.out.println(listItem);
				readerWriter.readLargerTextFile(listItem, lines);
			}
			readerWriter.writeLargerTextFile(targetTextFilePath, lines);
			
			// Pause
			System.out.println("Press Enter key to continue");
			System.in.read();

			// Extract not unzipping recursively
			String folderExtractedTo = unzipper.unzip(inputFilePath, tempFolderString, "zip", false);
			
			// Parse
			String targetPhonesFilePath = folderExtractedTo + File.separator + targetPhonesFileName;
			String targetEmailsFilePath = folderExtractedTo + File.separator + targetEmailsFileName;
			
			PhoneParser phoneParser = new PhoneParser();
			phoneParser.parse(lines, targetPhonesFilePath);
			
			EmailParser emailParser = new EmailParser();
			emailParser.parse(lines, targetEmailsFilePath);
			
			ZipPacker.zipDir(outputFilePath, folderExtractedTo);
			
			tempFolderFile.deleteOnExit();
			System.out.println("Temp folder is requested to be deleted on exit: " + tempFolderString);
			
			logger.trace("Exiting application.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
