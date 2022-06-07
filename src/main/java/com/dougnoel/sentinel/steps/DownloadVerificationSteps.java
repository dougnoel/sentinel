package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

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
}
