package com.dougnoel.sentinel.filemanagers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {
	
	private FileManager() {} //Exists to defeat instantiation.

	/**
	 * Take the path of a javscript file in linux format and converts it to load on any OS.
	 * (E.G. "src/main/resources/scripts/DragDrop.js")
	 * It returns the javascript file contents as a String for execution.
	 * 
	 * @param path String The path to find the javascript file.
	 * @return String The javascript file contents.
	 * @throws IOException if the file cannot be read
	 */
	
	public static String loadJavascript(String path) throws IOException {
		path = path.replaceAll("\\/", File.separator);
		byte[] encoded = Files.readAllBytes(Paths.get(path));
	    return new String(encoded, StandardCharsets.UTF_8);
	}
}
