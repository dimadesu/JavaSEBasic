package com.dataart.javasebasic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.dataart.javasebasic.file.RecursiveFileIterator;
import com.dataart.javasebasic.file.TextFileReaderWriter;
import com.dataart.javasebasic.parsers.EmailParser;
import com.dataart.javasebasic.parsers.PhoneParser;
import com.dataart.javasebasic.zip.ZipPacker;
import com.dataart.javasebasic.zip.ZipUnpacker;

public class App {
	
	public static final Logger logger = LogManager.getLogger("AppLogger");
	
	public static final String inputFileName = "inputs.zip";
	public static final String outputFileName = "inputsv2.zip";
	public static final String ARCHIVE_INDICATOR = "_extracted";
	public static final String ARCHIVE_NOT_FOUND = "archive_not_found";
	
	public static void main(String[] args) {

		try {
			
			App.logger.info("Entering application.");
			
			String inputFolder = null;
			File jarFile = null;
			
			// Path to the folder jar is executed from
			String jarPath = App.class.getProtectionDomain().getCodeSource()
					.getLocation().getPath();
			String jarDecodedPath = null;
			String jarFolder = null;
			try {
				jarDecodedPath = URLDecoder.decode(jarPath, "UTF-8");
				jarFile = new File(jarDecodedPath);
				jarFolder = jarFile.getParent() + "/";
				App.logger.debug("jarFolder: " + jarFolder);
			} catch (UnsupportedEncodingException e1) {
				App.logger.error("UnsupportedEncodingException", e1);
			}
			
			// If path was not supplied via program argument, then search for zip inside the folder jar is executed from
			if(args.length == 0) {
				inputFolder = jarFolder;
			} else {
				inputFolder = args[0] + "/";
			}
			
			// Input. Output
			File outputFile = new File (jarFolder + App.outputFileName + App.ARCHIVE_INDICATOR);
			String tempFolderName = jarFolder + App.inputFileName + App.ARCHIVE_INDICATOR;
			File tempFolderFile = new File(tempFolderName);
			if(!tempFolderFile.exists()) {
				tempFolderFile.mkdir();
			}
			App.logger.debug("Temp folder created: " + tempFolderName);
			
			// Target paths for parsed file
			String targetFileName = "phones.txt_emails.txt";
			String targetPhonesFileName = "phones.txt";
			String targetEmailsFileName = "emails.txt";
			String targetTextFilePath = tempFolderName + File.separator + targetFileName;

			App.logger.info("About to start unarchiving and collecting text files");
			
			// Unarchive recursively. Collect text files
			ZipUnpacker unzipper = new ZipUnpacker();
			
			String inputFilePath;
			String recusiveUnzipResult;

			do {
				
				inputFilePath = inputFolder + inputFileName;
				recusiveUnzipResult = unzipper.unzip(inputFilePath, jarFolder, "zip", true);

				if(recusiveUnzipResult == App.ARCHIVE_NOT_FOUND) {
				
					App.logger.info("Please enter path to the folder constaining inputs.zip");
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					inputFolder = br.readLine() +  "/";
				
				}
					
			} while (recusiveUnzipResult == App.ARCHIVE_NOT_FOUND);

			TextFileReaderWriter readerWriter = new TextFileReaderWriter();
			
			// Replace phone codes
	        RecursiveFileIterator recurser = new RecursiveFileIterator();
	        recurser.iterateDirectoryContents(new File(recusiveUnzipResult), null, false);
			
			// Log all the text files found
			List<String> lines = new ArrayList<String>();
			for (String listItem : App.textFiles) {
				App.logger.debug(listItem);
				readerWriter.readLargerTextFile(listItem, lines);
			}
			
			App.logger.debug("Lines collected:");
			for (String lineItem : lines) {
				App.logger.debug(lineItem);
			}
			
			App.logger.debug("Writing collected lines into a file.");
			readerWriter.writeLargerTextFile(targetTextFilePath, lines);
			
			// Parse
			String targetPhonesFilePath = tempFolderName + File.separator + targetPhonesFileName;
			String targetEmailsFilePath = tempFolderName + File.separator + targetEmailsFileName;
			
			PhoneParser phoneParser = new PhoneParser();
			phoneParser.parse(lines, targetPhonesFilePath);
			
			EmailParser emailParser = new EmailParser();
			emailParser.parse(lines, targetEmailsFilePath);
			
			new File (targetTextFilePath).delete();
			
			tempFolderFile.renameTo(outputFile);
			ZipPacker.checkFolder(outputFile.getParentFile());
			
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
			App.logger.error("IOException. Problem reading user input.", e);
		}
	}

}
