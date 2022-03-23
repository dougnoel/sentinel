package com.dougnoel.sentinel.filemanagers;

import java.awt.image.BufferedImage;
import java.io.File;
import org.apache.commons.io.FileUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.github.romankh3.image.comparison.ImageComparisonUtil;

public class FileManager {
	private static final Logger log = LogManager.getLogger(FileManager.class);
	
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
		path = path.replace("/", File.separator);
		byte[] encoded = Files.readAllBytes(Paths.get(path));
	    return new String(encoded, StandardCharsets.UTF_8);
	}
	
	/**
	 * Returns the absolute path to the file searching the root directory of the project
	 * and any sub directories.
	 * 
	 * @param fileName String the full (exact) name of file to be found
	 * @return File the path of the file found
	 */
	public static File findFilePath(String fileName)  {
		File result = searchDirectory(new File("src/"), fileName);

		if (result == null) {
			var errorMessage = SentinelStringUtils.format("Failed to locate the {} file. Please ensure the file exists in the src directory or its subdirectories.", fileName);
			throw new FileException(errorMessage, new FileNotFoundException(), new File(fileName));
		}

		return result;
	}
	
	/**
	 * Returns a File handler to a file if it is found in the given directory or any
	 * sub-directories.
	 * 
	 * @param directory File the directory to start the search
	 * @param fileName String the full name of the file with extension to find
	 * @return File the file that is found, null if nothing is found
	 */
	protected static File searchDirectory(File directory, String fileName) {
		log.trace("Searching directory {}", directory.getAbsoluteFile());
		File searchResult = null;
		if (directory.canRead()) {
			for (File temp : directory.listFiles()) {
				if (temp.isDirectory()) {
					searchResult = searchDirectory(temp, fileName);
				} else {
					if (fileName.equals(temp.getName()))
						searchResult = temp.getAbsoluteFile();
				}
				if (searchResult != null) {
					break;
				}
			}
		} else {
			throw new FileException(new AccessDeniedException("Access denied."), directory);
		}
		return searchResult;
	}

	/**
	 * Returns a valid class path for instantiating a java class given a class name.
	 * 
	 * @param className String the name of the class (case sensitive)
	 * @return String the path to the class that can be used to create an object
	 */
	public static String getClassPath(String className) {
		try {
			String filePath = findFilePath(className + ".java").getPath();
			String returnValue = StringUtils.removeEnd(filePath, ".java").replace(File.separator, ".");
			return StringUtils.substringAfter(returnValue, "java.");
		} catch (FileException fe) {
			return null;
		}
	}
	
	/**
	 * Saves an image File to an optionally set sub-directory of the configured, console set, or default "logs/images" directory.
	 * Can optionally save to the root image directory if subDirectory is null.
	 * 
	 * @param subDirectory String the sub-directory to use
	 * @param fileName String the file name of the image
	 * @param imageFile File the File of the image to save
	 */
	public static void saveImage(String subDirectory, String fileName, File imageFile) throws IOException {
		FileUtils.copyFile(imageFile, constructImagePath(subDirectory, fileName));
	}
	
	/**
	 * Saves a BufferImage to an optionally set sub-directory of the configured, console set, or default "logs/images" directory.
	 * Can optionally save to the root image directory if subDirectory is null.
	 * 
	 * @param subDirectory String the sub-directory to use
	 * @param fileName String the file name of the image
	 * @param imageFile BufferdImage the BufferedImage to save
	 */
	public static void saveImage(String subDirectory, String fileName, BufferedImage imageFile) throws IOException {
		File destinationFile = constructImagePath(subDirectory, fileName);
		if(!destinationFile.mkdirs()) {
			throw new IOException("Failed to create the full path for: " + destinationFile.getAbsolutePath());
		}
		
		ImageIO.write(imageFile, "png", destinationFile);
	}

	/**
	 * Reads an image from disk from the configured, console set, or default "logs/images" directory.
	 * Optionally can read from the root directory if subDirectory is null.
	 * 
	 * @param subDirectory String the sub-directory to use
	 * @param fileName String the file name of the image
	 * 
	 * @return BufferedImage the image file read from disk
	 */
	public static BufferedImage readImage(String subDirectory, String fileName) {
		return ImageComparisonUtil.readImageFromResources(constructImagePath(subDirectory, fileName).getAbsolutePath());
	}
	
	 /**
	 * Returns a String of the directory to use for test images set in the config file, command line,
	 * or alternatively "logs/images" by default.
	 * 
	 * @return String the configured, console set, or default directory if none is found
	 */
	public static String getImageDirectory() {
		String imageDirectory = Configuration.toString("imageDirectory");
	    if(imageDirectory == null) {
	    	imageDirectory = "logs/images";
		}
	    
	    return imageDirectory;
	}
    
	/**
	* Returns a File use for saving an image using an optional sub-directory,
	* file name, and the configured, console set, or default "logs/images" directory.
	* Will use the root directory if subDirectory is null.
	* 
	* @param subDirectory String the sub-directory to use
	* @param fileName String the file name of the image
	* 
	* @return File the constructed image File from directories and filename
	*/
	private static File constructImagePath(String subDirectory, String fileName) {
		if(subDirectory == null) 
			subDirectory = "";
		else
			subDirectory += "/";
		
		return new File(getImageDirectory() + "/" + subDirectory + fileName);
	}
}
