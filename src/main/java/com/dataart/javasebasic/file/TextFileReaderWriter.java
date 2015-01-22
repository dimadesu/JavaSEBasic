package com.dataart.javasebasic.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextFileReaderWriter {
	
	private final Logger logger = LogManager.getLogger("AppLogger");

	final static Charset ENCODING = StandardCharsets.UTF_8;

	// For smaller files
	
	List<String> readSmallTextFile(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		return Files.readAllLines(path, ENCODING);
	}

	public void writeSmallTextFile(List<String> aLines, String aFileName)
			throws IOException {
		Path path = Paths.get(aFileName);
		Files.write(path, aLines, ENCODING);
	}

	// For larger files

	public void readLargerTextFile(String aFileName, Set<String> linesAllFiles) {
		Path path = Paths.get(aFileName);
		logger.debug("Reading file: " + path);
		try (Scanner scanner = new Scanner(path, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				// process each line in some way
				String line = scanner.nextLine();
				if (!line.isEmpty()) {
					linesAllFiles.add(line);
				}
			}
		} catch (IOException e) {
			logger.error("Could not read text file.", e);
		}
	}

	public void readLargerTextFileAlternate(String aFileName, Set<String> linesAllFiles) {
		Path path = Paths.get(aFileName);
		logger.debug("Reading file: " + path);
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				// process each line in some way
				if (!line.isEmpty()) {
					linesAllFiles.add(line);
				}
			}
		} catch (IOException e) {
			logger.error("Could not read text file.", e);
		}
	}

	public void writeLargerTextFile(String aFileName, List<String> aLines) {
		Path path = Paths.get(aFileName);
		logger.debug("Writing file: " + path);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)) {
			for (String line : aLines) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			logger.error("Could not write file.", e);
		}
	}

}
