package com.dougnoel.sentinel.system;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

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
}
