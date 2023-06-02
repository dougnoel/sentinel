package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.dougnoel.sentinel.files.ZipFile;
import io.cucumber.java.en.When;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.system.DownloadManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.Then;

public class DownloadVerificationSteps {
	private static final String TRAILING_DOWNLOAD_FAILURE_MESSAGE = "Perhaps the download did not complete in time. Check your timeout.";

	/**
	 * Performs a click on the given element and uses the DownloadManager to monitor the default (or previously set) download directory and waits for a new file with the given extension to appear.
	 * <p>
	 * <b>Gherkin Example:</b><br>
	 * - I verify that by clicking the download link a new file is downloaded with the extension docx
	 * <p>
	 * @param elementName name of the element to click to start the download
	 * @param fileExtension String the file extension to wait for. Do not use a leading period.
	 * @throws InterruptedException When the thread is interrupted
	 * @throws IOException When a file or folder cannot be opened or any generic IO error occurs
	 */
	@Then("^I verify that by clicking (?:a|an|the) (.*?) a new file is downloaded with the extension (.*?)$")
	public static void verifyFileWithExtensionDownloadedWithClick(String elementName, String fileExtension) throws InterruptedException, IOException {
		DownloadManager.setFileExtension(fileExtension);
		var fileName = DownloadManager.monitorDownload(() -> BaseSteps.click(elementName));
		String expectedResult = SentinelStringUtils.format("Expected a new file with the {} extension to be downloaded. "
						+ TRAILING_DOWNLOAD_FAILURE_MESSAGE,
				fileExtension);

		assertTrue(expectedResult, !StringUtils.isEmpty(fileName));
	}

	/**
	 * Uses the DownloadManager to monitor the default (or previously set) download directory and waits for a new file with the given extension to appear.
	 * <p>
     * <b>Gherkin Example:</b><br>
     * - I verify a new file is downloaded with the extension docx
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
				+ TRAILING_DOWNLOAD_FAILURE_MESSAGE,
                fileExtension);
		
		assertTrue(expectedResult, !StringUtils.isEmpty(fileName));
	}

	/**
	 * Performs a click on the given element and uses the DownloadManager to monitor the default (or previously set) download directory and waits for a new file with the given filename to appear.
	 * <p>
	 * <b>Gherkin Example:</b><br>
	 * - I verify that by clicking the download link a new file is downloaded with the name example.docx
	 * <p>
	 * @param elementName name of the element to click to start the download
	 * @param expectedFilename String the file extension to wait for. Do not use a leading period.
	 * @throws InterruptedException When the thread is interrupted
	 * @throws IOException When a file or folder cannot be opened or any generic IO error occurs
	 */
	@Then("^I verify that by clicking (?:a|an|the) (.*?) a new file is downloaded with the name (.*?)$")
	public static void verifyFileWithFilenameDownloadedWithClick(String elementName, String expectedFilename) throws InterruptedException, IOException {
		DownloadManager.setFileExtension(FilenameUtils.getExtension(expectedFilename));
		var downloadedFilename = DownloadManager.monitorDownload(() -> BaseSteps.click(elementName));
		String expectedResult = SentinelStringUtils.format("Expected a new file with the filename \"{}\"  to be downloaded. "
						+ TRAILING_DOWNLOAD_FAILURE_MESSAGE,
				expectedFilename);

		assertTrue(expectedResult, StringUtils.equals(downloadedFilename, expectedFilename));
	}

	/**
	 * Uses the DownloadManager to monitor the default (or previously set) download directory and waits for a new file with the given filename to appear.
	 * <p>
	 * <b>Gherkin Example:</b><br>
	 * - I verify a new file is downloaded with the name example.docx
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
						+ TRAILING_DOWNLOAD_FAILURE_MESSAGE,
				expectedFilename);

		assertTrue(expectedResult, StringUtils.equals(downloadedFilename, expectedFilename));
	}

	/**
	 * Uses the DownloadManager to fetch the most recently downloaded file path.
	 * Then verifies the filename contains the current date time in <b>YYYY_MMM_dd</b> format by default or a supplied format.
	 * <p>
	 * <br><b>Gherkin Example:</b><br>
	 * <ul>
	 * <li>I verify the filename contains today's date</li>
	 * <li>I verify the filename contains today's date <i>formatted as <b>YYYY_MMM_dd_hh:ss:mm</b></i></li>
	 * </ul>
	 * <p>
	 * @param dateTimePattern String an optional datetime pattern
	 */
	@Then("^I verify the filename contains today's date(?: formatted as )?(.*?)$")
	public static void verifyFileWithDateTimeFilename(String dateTimePattern) {
		if(dateTimePattern.isBlank())
			dateTimePattern = "YYYY_MMM_dd";

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
		String formattedDateTime = dateFormatter.format(LocalDateTime.now());

		String mostRecentDownloadFilename = DownloadManager.getMostRecentDownloadPath().getFileName().toString();
		String expectedResult = SentinelStringUtils.format("Expected a new file containing the current date time \"{}\" to be downloaded. "
						+ TRAILING_DOWNLOAD_FAILURE_MESSAGE,
				formattedDateTime);

		assertTrue(expectedResult, StringUtils.contains(mostRecentDownloadFilename, formattedDateTime));
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
	 * <p>This method is <b>case insensitive</b> for file extension.</p>
	 * <p>
	 * Will behave the same as the steps:
	 * <ul>
	 * <li>I verify the most recently downloaded zip file contains <i><b>any</b></i> files <i>with the extension <b>zip</b></i></li>
	 * <li>I verify the most recently downloaded zip file <i><b>does not</b></i> contain <i><b>any</b></i> files <i>with the extension <b>pDf</b></i></li>
	 * </ul>
	 * </p>
	 * @param assertion String " does not" for a negative check, otherwise positive check
	 * @param expectedFileType String the file extension to check for.
	 */
	@Then("^I verify the most recently downloaded zip file( does not)? contains? a file with the extension (.*?)$")
	public static void verifyFileContentsOfZip(String assertion, String expectedFileType) throws IOException {
		verifyZipContentsFileCount(assertion, "any", expectedFileType);
	}

	/**
	 * Looks inside the most recently downloaded zip file and asserts that it does or does not contain a give number of files
	 * with an optionally specified extension.
	 * <p>This method can find files in subdirectories inside the zip, but not within nested zip files.</p>
	 * <p>This method is <b>case insensitive</b> for file extension.</p>
	 *
	 * <p>
	 * <br><b>Gherkin Example:</b><br>
	 * <ul>
	 * <li>I verify the most recently downloaded zip file <i><b>does not</b></i> contain <i><b>any</b></i> files <i>with the extension <b>pDf</b></i></li>
	 * <li>I verify the most recently downloaded zip file <i><b>does not</b></i> contain <i><b>any</b></i> files <i>with the extension <b>csv</b></i></li>
	 * <li>I verify the most recently downloaded zip file contains <i><b>any</b></i> files <i>with the extension <b>txT</b></i></li>
	 * <li>I verify the most recently downloaded zip file contains <i><b>1</b></i> file <i>with the extension <b>csv</b></i></li>
	 * <li>I verify the most recently downloaded zip file contains <i><b>10</b></i> files</li>
	 * <li>I verify the most recently downloaded zip file <i><b>does not</b></i> contain <i><b>11</b></i> files</li>
	 * <li>I verify the most recently downloaded zip file contains <i><b>0</b></i> files <i>with the extension <b>doc</b></i></li>
	 * </ul>
	 * <p>
	 *
	 * @param assertion String " does not" for a negative check, otherwise positive check.
	 * @param expectedFileCount String the number of files we expect. 'Any' indicates that it will check for a count above 0.
	 * @param expectedFileType String the optional file extension to limit the check to.
	 */
	@Then("^I verify the most recently downloaded zip file( does not)? contains? (\\d+|any) files?(?: with the extension (.*?))?$")
	public static void verifyZipContentsFileCount(String assertion, String expectedFileCount, String expectedFileType) throws IOException {
		Path mostRecentFile = DownloadManager.getMostRecentDownloadPath();
		List<String> fileContent;
		long expectedNumOfFiles = 0;
		if(!expectedFileCount.contains("any"))
			expectedNumOfFiles = Long.parseLong(expectedFileCount);

		boolean result;
		boolean negate = !StringUtils.isEmpty(assertion);

		try{
			ZipFile zip = new ZipFile(DownloadManager.getMostRecentDownloadPath());

			fileContent = zip.getFileNames();
			long filesFound;
			if(expectedFileType != null) {
				filesFound = fileContent.stream().filter(fileName -> StringUtils.endsWith(fileName.toLowerCase(), expectedFileType.toLowerCase())).count();
			}
			else
				filesFound = zip.getFileNames().size();

			if(expectedFileCount.contains("any"))
				result = filesFound > 0;
			else
				result = (expectedNumOfFiles == filesFound);
		}
		catch(IOException ioe){
			String message = SentinelStringUtils.format("Unable to open most recently downloaded file as zip file. Most recently downloaded file path {}", mostRecentFile);
			throw new IOException(message, ioe);
		}
		assertEquals(SentinelStringUtils.format("Expected zip file to {}contain {} files with extension {}. Zip file location: {} Files in zip: {}",
						(negate ? "not ": ""), expectedFileCount, expectedFileType, mostRecentFile, StringUtils.join(fileContent, ", ")),
				!negate, result);
	}
}