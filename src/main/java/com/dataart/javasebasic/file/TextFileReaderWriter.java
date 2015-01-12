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

import com.dataart.javasebasic.App;

public class TextFileReaderWriter {

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

	public void readLargerTextFile(String aFileName, List<String> linesAllFiles) {
		Path path = Paths.get(aFileName);
		Integer lineCounter = 0;
		App.logger.debug("Reading file: " + path);
		try (Scanner scanner = new Scanner(path, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				// process each line in some way
				String line = scanner.nextLine();
				if (!linesAllFiles.contains(line) && !line.isEmpty()) {
					linesAllFiles.add(line);
				}
				lineCounter++;
			}
		} catch (IOException e) {
			App.logger.error("Could not read text file.", e);
		}
	}

	public List<String> readLargerTextFileAlternate(String aFileName)
			throws IOException {
		Path path = Paths.get(aFileName);
		List<String> lines = new ArrayList<String>();
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				// process each line in some way
				lines.add(line);
			}
		}
		return lines;
	}

	public void writeLargerTextFile(String aFileName, List<String> aLines) {
		Path path = Paths.get(aFileName);
		App.logger.debug("Writing file: " + path);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)) {
			for (String line : aLines) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			App.logger.error("Could not write file.", e);
		}
	}

}
