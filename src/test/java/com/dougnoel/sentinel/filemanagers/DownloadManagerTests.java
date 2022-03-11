package com.dougnoel.sentinel.filemanagers;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Test;

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
	public void testVerifyPDFComparisonViaRendering() {
		File file = new File("src/test/resources/pdfs/TestPDF.pdf");
		File file2 = new File("src/test/resources/pdfs/TestPDF_copy.pdf");
		assertTrue("Expecting PDF to match a copy of itself.", DownloadManager.verifyDownloadedPDFViaVisualRendering(file, file2));
	}
	
	@Test
	public void testVerifyPDFComparisonViaRendering_Negative() {
		File file = new File("src/test/resources/pdfs/TestPDF.pdf");
		File file2 = new File("src/test/resources/pdfs/TestPDF_different.pdf");
		assertFalse("Expecting PDF to match a copy of itself.", DownloadManager.verifyDownloadedPDFViaVisualRendering(file, file2));
	}
}
