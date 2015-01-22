package com.dataart.javasebasic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.dataart.javasebasic.file.RecursiveFileIterator;
import com.dataart.javasebasic.file.TextFileReaderWriter;
import com.dataart.javasebasic.parsers.EmailParser;
import com.dataart.javasebasic.parsers.PhoneParser;
import com.dataart.javasebasic.zip.ZipPacker;
import com.dataart.javasebasic.zip.ZipUnpacker;

public class App {
	
	private final static Logger logger = LogManager.getLogger("AppLogger");
	
	public static final String inputFileName = "inputs.zip";
	public static final String outputFileName = "inputsv2.zip";
	public static final String ARCHIVE_INDICATOR = "_extracted";
	public static final String ARCHIVE_NOT_FOUND = "archive_not_found";
	
	public static void main(String[] args) {

		try {
			
			logger.info("Entering application.");
			
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
				logger.debug("jarFolder: " + jarFolder);
			} catch (UnsupportedEncodingException e) {
				logger.error("UnsupportedEncodingException", e);
			}
			
			// If path was not supplied via program argument, then search for zip inside the folder jar is executed from
			if(args.length == 0) {
				inputFolder = jarFolder;
			} else {
				inputFolder = args[0] + "/";
			}
			
			String inputFilePath;
			Boolean isZipPathValid = false;

			do {
				
				inputFilePath = inputFolder + inputFileName;
				
				isZipPathValid = ZipPacker.isZipValid(new File(inputFilePath));
				if(!isZipPathValid) {
				
					logger.info("Please enter path to the folder constaining inputs.zip");
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					inputFolder = br.readLine() +  "/";
				
				}
					
			} while (!isZipPathValid);
			
			// Input. Output
			File outputFile = new File (jarFolder + App.outputFileName + App.ARCHIVE_INDICATOR);
			String tempFolderName = jarFolder + App.inputFileName + App.ARCHIVE_INDICATOR;
			File tempFolderFile = new File(tempFolderName);
			// If it there is a leftover folder from last time delete it
			if(tempFolderFile.exists()) {
				ZipPacker.deleteAnything(tempFolderFile);
			}
			tempFolderFile.mkdir();
			logger.debug("Temp folder created: " + tempFolderName);

			logger.info("About to start unarchiving and collecting text files");
			
			// Unarchive recursively. Collect text files
			ZipUnpacker unzipper = new ZipUnpacker();
			String recusiveUnzipResult = unzipper.unzip(inputFilePath, jarFolder, "zip", true);
			
			// Replace phone codes
	        RecursiveFileIterator recurser = new RecursiveFileIterator();
	        recurser.iterateDirectoryContents(new File(recusiveUnzipResult), null, false);
			
			// Log all the text files found
	        // Using Set will get rid of duplicates 
	        Set<String> linesSet = new HashSet<String>();
			TextFileReaderWriter readerWriter = new TextFileReaderWriter();
			for (String listItem : App.textFiles) {
				logger.debug(listItem);
				readerWriter.readLargerTextFileAlternate(listItem, linesSet);
			}
	        // Convert Set to List, since lists can be sorted
			List<String> lines = new ArrayList<String>(linesSet);
			
			logger.debug("Lines collected:");
			for (String lineItem : lines) {
				logger.debug(lineItem);
			}
			
			// Target paths for parsed file
			String targetPhonesFileName = "phones.txt";
			String targetEmailsFileName = "emails.txt";
			
			// Parse
			String targetPhonesFilePath = tempFolderName + File.separator + targetPhonesFileName;
			String targetEmailsFilePath = tempFolderName + File.separator + targetEmailsFileName;
			
			PhoneParser phoneParser = new PhoneParser();
			phoneParser.parse(lines, targetPhonesFilePath);
			
			EmailParser emailParser = new EmailParser();
			emailParser.parse(lines, targetEmailsFilePath);
			
			tempFolderFile.renameTo(outputFile);
			ZipPacker.checkFolder(outputFile.getParentFile());
			
			logger.info("Exiting application.");

		} catch (IOException e) {
			logger.fatal("IOException. Exiting application.", e);
		} catch (Exception e) {
			logger.fatal("Unknown exception. Exiting application.", e);
		}

	}
	
	public static int textFilesCounter = 0;
	public static List<String> textFiles = new ArrayList<String>();

	public static void textFileFound(String path) {
		App.textFilesCounter++;
		App.textFiles.add(path);
		logger.info("Text file found. Total text files so far: "
				+ textFilesCounter);
	}

}
