package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.system.DownloadManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.Then;

public class DownloadVerificationSteps {

	/**
	 * Uses the DownloadManager to monitor the default (or previously set) download directory and waits for a new file with the given extension to appear.
	 * <p>
     * <b>Gherkin Example:</b>
     * <li>I verify a new docx file is downloaded</li>
     * <p>
	 * @param fileExtension String the file extension to wait for. Do not use a leading period.
	 * @throws InterruptedException When the thread is interrupted
	 * @throws IOException When a file or folder cannot be opened or any generic IO error occurs
	 */
	@Then("^I verify a new (.*) file is downloaded$")
	public static void verifyFileWithExtensionDownloaded(String fileExtension) throws InterruptedException, IOException {
		DownloadManager.setFileExtension(fileExtension);
		var fileName = DownloadManager.monitorDownload();
		String expectedResult = SentinelStringUtils.format("Expected a new file with the {} extension to be downloaded. "
				+ "Perhaps the download did not complete in time. Check your timeout.",
                fileExtension);
		
		assertTrue(expectedResult, !StringUtils.isEmpty(fileName));
	}
}