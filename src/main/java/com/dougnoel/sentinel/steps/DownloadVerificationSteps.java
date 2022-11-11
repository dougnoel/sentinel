package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.dougnoel.sentinel.files.ZipFile;
import io.cucumber.java.en.When;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.system.DownloadManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.Then;

public class DownloadVerificationSteps {

	/**
	 * Uses the DownloadManager to monitor the default (or previously set) download directory and waits for a new file with the given extension to appear.
	 * <p>
     * <b>Gherkin Example:</b><br>
     * - I verify a new docx file is downloaded
     * <p>
	 * @param fileExtension String the file extension to wait for. Do not use a leading period.
	 * @throws InterruptedException When the thread is interrupted
	 * @throws IOException When a file or folder cannot be opened or any generic IO error occurs
	 */
	@Then("^I verify a new file is downloaded with the extension (.*?)$")
	public static void verifyFileWithExtensionDownloaded(String fileExtension) throws InterruptedException, IOException {
		DownloadManager.setFileExtension(fileExtension);
		var fileName = DownloadManager.monitorDownload();
		String expectedResult = SentinelStringUtils.format("Expected a new file with the {} extension to be downloaded. "
				+ "Perhaps the download did not complete in time. Check your timeout.",
                fileExtension);
		
		assertTrue(expectedResult, !StringUtils.isEmpty(fileName));
	}

	/**
	 * Uses the DownloadManager to monitor the default (or previously set) download directory and waits for a new file with the given filename to appear.
	 * <p>
	 * <b>Gherkin Example:</b><br>
	 * - I verify a new file with the name "example.docx" is downloaded
	 * <p>
	 * @param expectedFilename String the filename to wait for. Do not need to fully qualify the path.
	 * @throws InterruptedException When the thread is interrupted
	 * @throws IOException When a file or folder cannot be opened or any generic IO error occurs
	 */
	@Then("^I verify a new file is downloaded with the name (.*?)$")
	public static void verifyFileWithFilenameDownloaded(String expectedFilename) throws InterruptedException, IOException {
		DownloadManager.setFileExtension(FilenameUtils.getExtension(expectedFilename));
		var downloadedFilename = DownloadManager.monitorDownload();
		String expectedResult = SentinelStringUtils.format("Expected a new file with the filename \"{}\"  to be downloaded. "
						+ "Perhaps the download did not complete in time. Check your timeout.",
				expectedFilename);

		assertTrue(expectedResult, StringUtils.equals(downloadedFilename, expectedFilename));
	}

	/**
	 * Clears all files from the download directory. Does not delete the directory itself.
     * @throws IOException When the directory is undefined / does not exist, or any generic IO error occurs.
	 */
	@When("I clear all files from the downloads folder")
	public static void clearDownloadsFolder() throws IOException {
		DownloadManager.clearDownloadDirectory();
	}

	/**
	 * Looks inside the most recently downloaded zip file and asserts that it does or does not contain a file with the given extension.
	 * This method can find files in subdirectories inside the zip, but not within nested zip files.
	 * @param assertion String " does not" for a negative check, otherwise positive check
	 * @param expectedFileType String the file extension to check for.
	 */
	@Then("^I verify the most recently downloaded zip file( does not)? contains? a file with the extension (.*?)$")
	public static void verifyFileContentsOfZip(String assertion, String expectedFileType) throws IOException {
		Path mostRecentFile = DownloadManager.getMostRecentDownloadPath();
		List<String> fileContent;
		boolean result;
		boolean negate = !StringUtils.isEmpty(assertion);

		try{
			ZipFile zip = new ZipFile(DownloadManager.getMostRecentDownloadPath());
			fileContent = zip.getFileNames();
			result = fileContent.stream().anyMatch(fileName -> StringUtils.endsWith(fileName, expectedFileType));
		}
		catch(IOException ioe){
			String message = SentinelStringUtils.format("Unable to open most recently downloaded file as zip file. Most recently downloaded file path {}", mostRecentFile);
			throw new IOException(message, ioe);
		}
		assertEquals(SentinelStringUtils.format("Expected zip file to {}contain a file with extension {}. Zip file location: {} Files in zip: {}",
						(negate ? "not ": ""), expectedFileType, mostRecentFile, StringUtils.join(fileContent, ", ")),
				!negate, result);
	}
}
