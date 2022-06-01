package com.dougnoel.sentinel.system;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.Driver;

import de.redsix.pdfcompare.RenderingException;

public class DownloadManagerTests {

	@Test
	public void testVerifyTextInDownloadedPDFPageOne() {
		File file = new File("src/test/resources/pdfs/TestPDF.pdf");
		assertTrue("Expecting text to appear on page 1 of PDF.", DownloadManager.verifyTextInDownloadedPDF("This is page one.", file, 1, 1));
	}
	
	@Test
	public void testVerifyTextNotInDownloadedPDFPageTwo() {
		File file = new File("src/test/resources/pdfs/TestPDF.pdf");
		assertFalse("Expecting text to not appear on page 2 of PDF.", DownloadManager.verifyTextInDownloadedPDF("This is page one.", file, 2, 2));
	}
	
	@Test
	public void testVerifyPDFComparisonViaRendering() throws RenderingException, IOException {
		File file = new File("src/test/resources/pdfs/TestPDF.pdf");
		File file2 = new File("src/test/resources/pdfs/TestPDF_copy.pdf");
		assertTrue("Expecting PDF to match a copy of itself.", DownloadManager.verifyDownloadedPDFViaVisualRendering(file, file2));
	}
	
	@Test
	public void testVerifyPDFComparisonViaRendering_Negative() throws RenderingException, IOException {
		File file = new File("src/test/resources/pdfs/TestPDF.pdf");
		File file2 = new File("src/test/resources/pdfs/TestPDF_different.pdf");
		assertFalse("Expecting PDFs to not match.", DownloadManager.verifyDownloadedPDFViaVisualRendering(file, file2));
	}
	
	@Test(expected = RuntimeException.class)
	public void testVerifyPDFComparisonViaRendering_OutputFileError() throws RenderingException, IOException {
		File file = new File("src/test/resources/pdfs/TestPDF.pdf");
		File file2 = new File("src/test/resources/pdfs/TestPDF_different.pdf");
		DownloadManager.verifyDownloadedPDFViaVisualRendering(file, file2, 0.0, "/not/a/real/path");
	}
	
	@Test
	public void verifyFileDownloadFromWebAndSavePDFImage() throws InterruptedException, IOException {
		BaseSteps.navigateToPage("RadioButtonPage");
		BaseSteps.click("sample_download_link");
		String filename = DownloadManager.monitorDownload();
		assertTrue("Expecting TestPDF.pdf to be downloaded.", DownloadManager.isFileDownloaded(filename));
		String savedImage = DownloadManager.saveImageInPDF(0, filename);
		assertTrue("Expecting "+savedImage+" from TestPDF.pdf to be downloaded.", DownloadManager.isFileDownloaded(savedImage));
		Driver.quitAllDrivers();
	}

	@Test
	public void verifyUrlPdfDownloadFromWeb() throws IOException {
		assertTrue("Verifying text on PDF downloaded via stream.",
				DownloadManager.verifyPDFContent(new URL("https://dougnoel.github.io/sentinel/test/TestPDF.pdf"), "This is page one.", 1, 1));
	}

	@Test(expected = IOException.class)
	public void verifyUrlStreamDownloadFromWebCannotOpenHtmlAsPdf() throws IOException {
		DownloadManager.verifyPDFContent(new URL("https://dougnoel.github.io/sentinel/test/radiobutton.html"), "This is page one.", 1, 1);
	}
	
}
