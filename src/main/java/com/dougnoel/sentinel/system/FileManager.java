package com.dougnoel.sentinel.system;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.commons.io.FileUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.github.romankh3.image.comparison.ImageComparisonUtil;

import static com.dougnoel.sentinel.configurations.Configuration.operatingSystem;
import static java.util.Map.entry;

public class FileManager {
	private static final Logger log = LogManager.getLogger(FileManager.class);
	private static final String IMAGE_DIRECTORY = "logs" + File.separator + "images";
	private static final Map<String, String> WINDOWS_SPECIAL_FOLDERS = Map.ofEntries(
		entry("%appdata%", System.getenv("APPDATA")),
		entry("%localappdata%", System.getenv("LOCALAPPDATA"))
	);
	private static final Map<String, String> LINUX_SPECIAL_FOLDERS = Map.ofEntries();
	private static final Map<String, String> MAC_SPECIAL_FOLDERS = Map.ofEntries();
	private static final String DETECTED_OS = operatingSystem();

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
		path = convertPathShortcuts(path);
		path = convertPathSeparators(path);
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
		File result = searchDirectory(new File("src" + File.separator), fileName);

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
		directory = new File(convertPathShortcuts(directory.getAbsolutePath()));
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
	 *
	 * @return File the resulting file object generated while saving to disk
	 */
	public static File saveImage(String subDirectory, String fileName, File imageFile) {
		File destinationFile = new File(getImagePath(subDirectory, fileName));

		try {
			FileUtils.copyFile(imageFile, destinationFile);
		} catch (IOException origException) {
			String errorMessage = SentinelStringUtils.format("Failed to save the image {} in the directory {}", fileName, destinationFile.getPath());
			throw new com.dougnoel.sentinel.exceptions.IOException(errorMessage, origException);
		}

		return destinationFile;
	}
	
	/**
	 * Saves a BufferImage to an optionally set sub-directory of the configured, console set, or default "logs/images" directory.
	 * Can optionally save to the root image directory if subDirectory is null.
	 * 
	 * @param subDirectory String the sub-directory to use
	 * @param fileName String the file name of the image
	 * @param imageFile BufferdImage the BufferedImage to save
	 *
	 * @return File the resulting file object generated while saving to disk
	 */
	public static File saveImage(String subDirectory, String fileName, BufferedImage imageFile) {
		File destinationFile = new File(getImagePath(subDirectory, fileName));

		try {
			FileUtils.forceMkdir(destinationFile.getParentFile());
			ImageIO.write(imageFile, "png", destinationFile);
		} catch (IOException origException) {
			String errorMessage = SentinelStringUtils.format("Failed to save the image {} in the directory {}", fileName, destinationFile.getPath());
			throw new com.dougnoel.sentinel.exceptions.IOException(errorMessage, origException);
		}

		return destinationFile;
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
		return ImageComparisonUtil.readImageFromResources(getImagePath(subDirectory, fileName));
	}

	/**
	 * Reads an image from disk using the path passed with a File Object
	 *
	 * @param filePath File the file object that refers to the location of the file
	 *
	 * @return BufferedImage the image file read from disk
	 */
	public static BufferedImage readImage(File filePath) {
		return ImageComparisonUtil.readImageFromResources(convertPathShortcuts(filePath.getAbsolutePath()));
	}
	
	/**
	* Returns the path for saving an image using the passed sub-directory,
	* file name, and the configured, console set, or default "logs/images" directory.
	* 
	* @param subDirectory String the sub-directory to use
	* @param fileName String the file name of the image
	* 
	* @return String the constructed path as a String
	*/
	private static String getImagePath(String subDirectory, String fileName) {
		String outputSubDir = "";

		if(subDirectory != null){
			outputSubDir = subDirectory;
		}

		return convertPathShortcuts(convertPathSeparators(Configuration.toString("imageDirectory", IMAGE_DIRECTORY) + File.separator + outputSubDir + File.separator + fileName));
	}

	/**
	 * Converts a path so all / characters are replaced with a system-independent separator
	 * @param path String the path to convert the separators of
	 * @return String a string path with system-independent separators
	 */
	public static String convertPathSeparators(String path) {
		return path.replace("/", File.separator);
	}

	/**
	 * Sanitizes paths so any special characters excluding periods and dashes are replaced with underscores
	 * @param toSanitize String the path to sanitize
	 * @return String a path with unallowed special characters replaced with "_"
	 */
	public static String sanitizeString(String toSanitize) {
		return toSanitize.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
	}

	/**
	 * Replaces special path shortcuts with their system environment equivalent within a given path
	 * <br><br>
	 * <b>Supported Path Shortcuts:</b>
	 * <li><i>%localappdata%</i></li>
	 * <li><i>%appdata%</i></li>
	 * @param pathToProcess String path to replace shortcuts
	 * @return String the path string with shortcuts replaced with their environment equivalent path
	 */
	public static String convertPathShortcuts(String pathToProcess){
		String originalPath = pathToProcess;

		Map<String, String> specialFolders = new HashMap<>();

		switch(DETECTED_OS){
			case "windows":
				specialFolders = WINDOWS_SPECIAL_FOLDERS;
				break;
			case "linux":
				specialFolders = LINUX_SPECIAL_FOLDERS;
				break;
			case "mac":
				specialFolders = MAC_SPECIAL_FOLDERS;
				break;
		}

		for(Map.Entry<String, String> entry : specialFolders.entrySet()){
			pathToProcess = StringUtils.replaceIgnoreCase(pathToProcess, entry.getKey(), entry.getValue());
			if(!pathToProcess.equals(originalPath)){
				break;
			}
		}

		return convertPathSeparators(pathToProcess);
	}
}