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
	
	public static final Logger logger = LogManager.getLogger("AppLogger");
	
	public static void main(String[] args) {

		try {
			
			App.logger.info("Entering application.");
			
			// Path to the folder jar is executed from
			String jarPath = App.class.getProtectionDomain().getCodeSource()
					.getLocation().getPath();
			String jarDecodedPath = null;
			String jarFolder = null;
			try {
				jarDecodedPath = URLDecoder.decode(jarPath, "UTF-8");
				File jarFile = new File(jarDecodedPath);
				jarFolder = jarFile.getParent() + "/";
				App.logger.debug("jarFolder: " + jarFolder);
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
			App.logger.debug("Temp folder created: " + tempFolderString);
			
			// Target paths for parsed file
			String targetFileName = "phones.txt_emails.txt";
			String targetPhonesFileName = "phones.txt";
			String targetEmailsFileName = "emails.txt";
			String targetTextFilePath = tempFolderString + File.separator + targetFileName;

			App.logger.info("About to start unarchiving and collecting text files");
			
			// Unarchive recursively. Collect text files
			ZipUnpacker unzipper = new ZipUnpacker();
			unzipper.unzip(inputFilePath, tempFolderString, "zip", true);

			// Log all the text files found in the end
			TextFileReaderWriter readerWriter = new TextFileReaderWriter();
			List<String> lines = new ArrayList<String>();
			for (String listItem : App.textFiles) {
				App.logger.debug(listItem);
				readerWriter.readLargerTextFile(listItem, lines);
			}
			App.logger.debug("Writing collected lines into a file.");
			readerWriter.writeLargerTextFile(targetTextFilePath, lines);

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
			App.logger.debug("Temp folder is requested to be deleted on exit: " + tempFolderString);
			
			App.logger.info("Program ran successfully.");
			App.logger.info("Exiting application.");

		} catch (IOException e) {
			App.logger.fatal("IOException. Exiting application.", e);
			App.pause();
		} catch (Exception e) {
			App.logger.fatal("Unknown exception. Exiting application.", e);
			App.pause();
		}

	}

	public static int textFilesCounter = 0;
	public static List<String> textFiles = new ArrayList<String>();

	public static void textFileFound(String path) {
		App.textFilesCounter++;
		App.textFiles.add(path);
		App.logger.info("Text file found. Total text files so far: "
				+ textFilesCounter);
	}
	
	public static void pause () {
		// Pause
		App.logger.info("Press Enter key to proceed");
		try {
			System.in.read();
		} catch (IOException e) {
			App.logger.error("IOException.", e);
		}
	}

}
