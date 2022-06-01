package com.dougnoel.sentinel.system;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import de.redsix.pdfcompare.RenderingException;
import de.redsix.pdfcompare.env.SimpleEnvironment;

/**
 * Manages Download actions and interactions, which handles CRUD and IO for Sentinel. This includes deleting files, getting/setting files, filenames, or file extensions,
 * monitoring downloads, verifying file has been downloaded, parsing PDF content for content verification, and 
 */
public class DownloadManager {
    private static final Logger log = LogManager.getLogger(DownloadManager.class.getName()); // Create a logger.

    private static String fileExtension = "pdf"; // Current file extension - Default of pdf

    private static String downloadDirectory = createDownloadDirectory();

    private DownloadManager(){}

    /**
     * Returns true if the file exists in the download directory.
     * 
     * @param fileName String the name of the file for which to look.
     * @return boolean Returns true if the file exists, false if it does not.
     */
    public static boolean isFileDownloaded(String fileName) {
    	var dir = new File(downloadDirectory);
        File[] directoryContents = dir.listFiles();

        for (var i = 0; i < directoryContents.length; i++) {
            if (directoryContents[i].getName().equals(fileName))
                return true;
        }

        return false;
    }

    /**
     * Monitors the current set download directory and returns a filename once the
     * file is downloaded.
     * 
     * @return String The name of the file that was downloaded.
     * @throws InterruptedException if the file download is interrupted
     * @throws IOException if the file cannot be created.
     */
    public static String monitorDownload() throws InterruptedException, IOException {
        return monitorDownload(downloadDirectory, fileExtension);
    }

    /**
     * Returns the name of a downloaded file by monitoring the given download directory and looking for a
     * file to be downloaded with the given file extension. It checks the given
     * directory every 1/10th of a second under the file download is complete, and
     * then returns the name of the file downloaded. If the download takes longer
     * than 20 seconds, the function times out and throws an error.
     * 
     * @param downloadDir String path to the download directory.
     * @param fileExtension String extension of the file type you are expecting to be  downloaded.
     * @return String The name of the file that was downloaded.
     * @throws InterruptedException if the thread is interrupted during download
     * @throws IOException if the file cannot be created.
     */
    public static String monitorDownload(String downloadDir, String fileExtension) throws InterruptedException, IOException {
        String downloadedFileName = null;
        var valid = true;
        
        long timeOut = Time.out().toSeconds();
        long loopTime = Time.loopInterval().toMillis();
        var downloadFolderPath = Paths.get(downloadDir);
        var watchService = FileSystems.getDefault().newWatchService();
        downloadFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        long startTime = System.currentTimeMillis();
        
        do {
            WatchKey watchKey;
            watchKey = watchService.poll(timeOut, TimeUnit.SECONDS);
            long currentTime = (System.currentTimeMillis() - startTime) / 1000;
            if (currentTime > timeOut || watchKey == null) {
                log.error("Download operation timed out. Expected file was not downloaded.");
                return downloadedFileName;
            }

            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                	var fileName = event.context().toString();
                    log.debug("New File Created: {}", fileName);
                    if (fileName.endsWith(fileExtension)) {
                        downloadedFileName = fileName;
                        log.debug("Downloaded file found: {}.{}", fileName, fileExtension);
                        Thread.sleep(loopTime);
                        return downloadedFileName;
                    }
                }
            }
            
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            if (currentTime > timeOut) {
                log.error("Failed to download expected file.");
                return downloadedFileName;
            }
            valid = watchKey.reset();
            
        } while (valid);
        
        return downloadedFileName;
    }

    /**
     * Returns true is the given text appears on a particular page of the passed PDF that is opened in a new tab,
     * without needing to download it. It takes the URL of the PDF which can be done
     * by using PageManager.getCurrentUrl() if the PDF is the active window. It also
     * takes a string of the text you expect to find, and the page number you expect
     * to find it on.
     * 
     * @param url String URL of the PDF
     * @param expectedText String Text to be checked in the PDF.
     * @param pageNumber int Number of the page on the pdf to look for the text.
     * @return boolean true if the text was found, false if it was not.
     * @throws IOException if there was an error in finding or opening the PDF
     */
    public static boolean verifyPDFContent(URL url, String expectedText, int pageNumber) throws IOException {
        return verifyPDFContent(url, expectedText, pageNumber, pageNumber);
    }

    /**
     * Returns true if the given text exists within a particular page range of the passed PDF. This PDF is opened in a new tab,
     * without needing to download it. It takes the URL of the PDF which can be done
     * by using PageManager.getCurrentUrl() if the PDF is the active window. It also
     * takes a string of the text you expect to find, and the page range you expect
     * to find it on. The page range is inclusive, meaning it will search the two
     * page numbers you pass it and every number in between. To search just one
     * page, pass the same number twice, or use the overloaded method that only
     * takes one int parameter.
     * 
     * @param url String strURL URL of the PDF
     * @param expectedText String Text to be checked in the PDF.
     * @param pageStart int Number of the page on the pdf to start looking for the text  (inclusive).
     * @param pageEnd int Number of the page on the pdf to stop looking for the text  (inclusive).
     * @return boolean true if the text was found, false if it was not.
     * @throws IOException if error while opening, stripping, loading, parsing, or closing PDF
     */
    public static boolean verifyPDFContent(URL url, String expectedText, int pageStart, int pageEnd) throws IOException {
    	var flag = false;

        BufferedInputStream file = null;
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        String parsedText = null;

        try {
            file = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
        	var errorMessage = SentinelStringUtils.format("Could not open the PDF file: {}", url.toString());
            throw new IOException(errorMessage, e);
        }

        try {
            pdfStripper = new PDFTextStripper();
        } catch (IOException e) {
        	var errorMessage = SentinelStringUtils.format("Could not create PDFTextStripper() for PDF file {} Setting -Dssltrust=all on the command line will bypass PKIX errors.", url.toString());
            throw new IOException(errorMessage, e);
        }
        pdfStripper.setStartPage(pageStart);
        pdfStripper.setEndPage(pageEnd);

        try {
            pdDoc = PDDocument.load(file);
        } catch (InvalidPasswordException e) {
        	var errorMessage = SentinelStringUtils.format("PDF file {} was password protected.", url.toString());
            throw new IOException(errorMessage, e);
        } catch (IOException e) {
        	var errorMessage = SentinelStringUtils.format("Could not load the PDF {}", url.toString());
            throw new IOException(errorMessage, e);
        }
        
        try {
            parsedText = pdfStripper.getText(pdDoc);
        } catch (IOException e) {
        	var errorMessage = SentinelStringUtils.format("Could not get text from PDFTextStripper() for PDF file {}", url.toString());
            throw new IOException(errorMessage, e);
        }

        if (pdDoc != null) {
            try {
                pdDoc.close();
            } catch (IOException e) {
            	var errorMessage = SentinelStringUtils.format("Could not close the PDF document {}", url.toString());
                throw new IOException(errorMessage, e);
            }
        }

        log.trace("PDF Parsed Text: \n{}", parsedText);

        if (parsedText.contains(expectedText)) {
            flag = true;
        }

        return flag;
    }
    /**
     * Returns true if the given text exists in the given PDF within a specified page range.
     * 
     * @param expectedText String Text to be checked in the PDF.
     * @param pdfFile File A File handler to the file
     * @param pageStart int Number of the page on the pdf to start looking for the text  (inclusive).
     * @param pageEnd int Number of the page on the pdf to stop looking for the text     (inclusive).
     * @return boolean true if the text was found, false if it was not.
     */
    public static boolean verifyTextInDownloadedPDF(String expectedText, File pdfFile, int pageStart, int pageEnd) {

    	var flag = false;

        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        String parsedText = null;

        try ( var fis = new FileInputStream(pdfFile);
        		var file = new BufferedInputStream(fis); ) {
        	
            pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(pageStart);
            pdfStripper.setEndPage(pageEnd);

            pdDoc = PDDocument.load(file);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (IOException e) {
            log.error("Unable to open PDF Parser. {}", e.getMessage());
            try {
                if (pdDoc != null)
                    pdDoc.close();
            } catch (Exception e1) {
                log.error(e1);
            }
        }
        
        log.trace("PDF Parsed Text: \n{}", parsedText);

        if (parsedText != null && parsedText.contains(expectedText)) {
            flag = true;
        }

        return flag;
    }
    
    /**
     * Verifies the content of the two files, specified by the given paths, have the same content based on a graphical pixel-to-pixel comparison.
     * The given percent of allowed difference per page is applied during the comparison, to allow for some expected variance. The default is 0.0 for an exact match.
     * The resulting file, with differences highlighted, is written to the given path.
     * @param expectedPdf File the file containing the reference PDF.
     * @param newPdf File the PDF file to compare to the reference PDF.
     * @param percentAllowedDifferencePerPage double the percent of allowed difference per page.
     * @param resultFilePath String the file path to write the result PDF to.
     * @return boolean true if the PDFs match, false otherwise
     * @throws IOException 
     * @throws RenderingException 
     */
    public static boolean verifyDownloadedPDFViaVisualRendering(File expectedPdf, File newPdf, double percentAllowedDifferencePerPage, String resultFilePath) throws RenderingException, IOException {
    	CompareResult result = new PdfComparator<>(expectedPdf, newPdf)
				.withEnvironment(new SimpleEnvironment()
				.setAllowedDiffInPercent(percentAllowedDifferencePerPage))
				.compare();
		
		if(resultFilePath != null && !StringUtils.isWhitespace(resultFilePath))
			result.writeTo(resultFilePath);
		
		return result.isEqual();
    }
    
    /**
     * Verifies the content of the two files, specified by the given paths, have the EXACT same content based on a graphical pixel-to-pixel comparison.
     * @param expectedPdf File the file containing the reference PDF.
     * @param newPdf File the PDF file to compare to the reference PDF.
     * @return boolean true if the PDFs match exactly, false otherwise
     * @throws IOException 
     * @throws RenderingException 
     */
    public static boolean verifyDownloadedPDFViaVisualRendering(File expectedPdf, File newPdf) throws RenderingException, IOException {
    	return verifyDownloadedPDFViaVisualRendering(expectedPdf, newPdf, 0.0, null);
    }
    
    /**
     * Returns the path of an image containing the entire contents of the PDF page given.
     * <br>
     * NOTE: The PDF is assumed to be in the downloads directory.
     * 
     * @param index int page index of the page to be rendered
     * @param pdfFileName String the name of the pdf containing image
     * @return String the location of the saved image file
     * @throws IOException if error during file IO or during document load
     */
    public static String saveImageInPDF(int index, String pdfFileName) throws IOException {
    	var pdfFile = new File(downloadDirectory + File.separator + pdfFileName);
    	var imageFile = new File(downloadDirectory + File.separator + pdfFileName + "_" + index + ".jpg");
        PDFRenderer pdfRenderer = null;
        BufferedImage image = null;

        var document = PDDocument.load(pdfFile);

        pdfRenderer = new PDFRenderer(document);
        image = pdfRenderer.renderImage(index);
        ImageIO.write(image, "JPEG", imageFile);
        document.close();

        return imageFile.getName();
    }

    /**
     * Sets given file extension
     * 
     * @param fileExtension String file ext to set
     */
    public static void setFileExtension(String fileExtension) {
        DownloadManager.fileExtension = fileExtension;
    }

    /**
     * Returns the download directory
     * 
     * @return String the downloadDirectory 
     */
    public static String getDownloadDirectory() {
        return downloadDirectory;
    }

    /**
     * Sets downloadDirectory upon creation of the Download manager.
     * @return String the download directory path
     */
    public static String createDownloadDirectory() {
    	var downloadDirectory = Configuration.toString("downloadDirectory");
    	if (downloadDirectory == null) {
    		String parentDirectoryPath = System.getProperty("user.dir");
    		Path downloadPath = Path.of(parentDirectoryPath, "/downloads/");
    		downloadPath.toFile().mkdir(); //make download directory if it doesn't already exist
    		downloadDirectory = downloadPath.toString();
    	}
        return downloadDirectory;
    }

    /**
     * Sets given downloadDirectory object
     * 
     * @param downloadDirectory String the downdloadDirectory to set
     */
    public static void setDownloadDirectory(String downloadDirectory) {
        DownloadManager.downloadDirectory = downloadDirectory;
    }

    /**
     * Clears the download directory. Does not delete the directory itself.
     * @throws IOException in the case that an IOException occurs while clearing the directory
     */
    public static void clearDownloadDirectory() throws IOException {
        FileUtils.cleanDirectory(new File(downloadDirectory));
    }
}