package com.dataart.javaSEBasic.file;

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

	public void readLargerTextFile(String aFileName, List<String> linesAllFiles) throws IOException {
		Path path = Paths.get(aFileName);
		Integer lineCounter = 0;
		try (Scanner scanner = new Scanner(path, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				// process each line in some way
				String line = scanner.nextLine();
				if (!linesAllFiles.contains(line) && !line.isEmpty()) {
					linesAllFiles.add(line);
					System.out.println("Added line:" + line);
				}
				lineCounter++;
				System.out.println("Processed line #" + lineCounter);
			}
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

	public void writeLargerTextFile(String aFileName, List<String> aLines)
			throws IOException {
		Path path = Paths.get(aFileName);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)) {
			for (String line : aLines) {
				writer.write(line);
				writer.newLine();
			}
		}
	}

	public static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
	}

}
