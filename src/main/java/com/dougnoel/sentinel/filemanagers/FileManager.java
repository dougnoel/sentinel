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
	 * Saves an image of type File to the given sub-directory of the configured, or default, image path.
	 * 
	 * @param subDirectory the sub-directory within the configured or default image path.
	 * If the sub-directory is null, the root image directory will be used.
	 * @param imageFileName the file name of the image to save.
	 * @param imageFile the File of the image to save.
	 */
	public static void saveImage(String subDirectory, String imageFileName, File imageFile) throws IOException {
		if(subDirectory == null) 
			subDirectory = "";
		else
			subDirectory += "/";
		
		File destinationFile = new File(Configuration.imageDirectory() + "/" + subDirectory + imageFileName);
    	FileUtils.copyFile(imageFile, destinationFile);
	}
	
	/**
	 * Saves an image of type BufferedImage to the given sub-directory of the configured, or default, image path.
	 * 
	 * @param subDirectory the sub-directory within the configured or default image path.
	 * If the subDirectory is null, the root image directory will be used.
	 * @param imageFileName the file name of the image to save.
	 * @param imageFile the BufferedImage of the image to save.
	 */
	public static void saveImage(String subDirectory, String imageFileName, BufferedImage imageFile) throws IOException {
		if(subDirectory == null) 
			subDirectory = "";
		else
			subDirectory += "/";
		
		File destinationFile = new File(Configuration.imageDirectory() + "/" + subDirectory + imageFileName);
		//ImageIO will not create directories. We have to make them ourselves before running the write command.
		FileUtils.forceMkdir(destinationFile.getParentFile());
    	ImageIO.write(imageFile, "png", destinationFile);
	}

	/**
	 * Reads an image from the sub-directory of the default or configured image directory.
	 * 
	 * @param sourceImageSubDirectory the sub-directory within the configured or default image path to read the file from.
	 * If the sub-directory is null, the root image directory will be used.
	 * @param imageFileName the file name of the image to read.
	 * 
	 * @return a BufferedImage of the image file on disk.
	 */
	public static BufferedImage readImage(String sourceImageSubDirectory, String imageFileName) {
		if(sourceImageSubDirectory == null) 
			sourceImageSubDirectory = "";
		else
			sourceImageSubDirectory += "/";
		
		return ImageComparisonUtil.readImageFromResources(Configuration.imageDirectory() + "/" + sourceImageSubDirectory + imageFileName);
	}
}
