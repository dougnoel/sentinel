package com.dougnoel.sentinel.filemanagers;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

/**
 * Manages Download actions and interactions, which handles CRUD and IO for Sentinel. This includes deleting files, getting/setting files, filenames, or file extensions,
 * monitoring downloads, verifying file has been downloaded, parsing PDF content for content verification, and 
 */
public class DownloadManager {
    private static final Logger log = LogManager.getLogger(DownloadManager.class.getName()); // Create a logger.

    private static DownloadManager instance = null;

    private static String filename = null; // Current filename
    private static String fileExtension = "pdf"; // Current file extension - Default of pdf
    private static File file = null; // Current file

    private static String downloadDirectory = createDownloadDirectory();

    // Create a list to store all the downloaded files
    // This will allow us to clean them all up at the end of a run.

    protected DownloadManager() {
        // Exists only to defeat instantiation.
    }

    /**
     * Creates instance of Singleton class.
     * @return DownloadManager instance of this class
     */
    public static DownloadManager getInstance() {
        if (instance == null)
            instance = new DownloadManager();

        return instance;
    }

    /**
     * Returns true if the file exists in the directory, given a filename and a path.
     * 
     * @param downloadPath String Path to the download directory.
     * @param fileName String The name of the file for which to look.
     * @return boolean Returns true if the file exists, false if it does not.
     */
    public static boolean isFileDownloaded(String downloadPath, String fileName) {
        File dir = new File(downloadPath);
        File[] directoryContents = dir.listFiles();

        for (int i = 0; i < directoryContents.length; i++) {
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
     */
    public static String monitorDownload() {
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
     */
    public static String monitorDownload(String downloadDir, String fileExtension) {
        String downloadedFileName = null;
        boolean valid = true;
        boolean found = false;

        // default timeout in seconds
        long timeOut = 20;
        try {
            Path downloadFolderPath = Paths.get(downloadDir);
            WatchService watchService = FileSystems.getDefault().newWatchService();
            downloadFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            long startTime = System.currentTimeMillis();
            do {
                WatchKey watchKey;
                watchKey = watchService.poll(timeOut, TimeUnit.SECONDS);
                long currentTime = (System.currentTimeMillis() - startTime) / 1000;
                if (currentTime > timeOut) {
                    log.error("Download operation timed out.. Expected file was not downloaded");
                    return downloadedFileName;
                }

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                        String fileName = event.context().toString();
                        log.debug("New File Created: {}", fileName);
                        if (fileName.endsWith(fileExtension)) {
                            downloadedFileName = fileName;
                            log.debug("Downloaded file found: {}.{}", fileName, fileExtension);
                            Thread.sleep(100);
                            found = true;
                            break;
                        }
                    }
                }
                if (found) {
                    return downloadedFileName;
                } else {
                    currentTime = (System.currentTimeMillis() - startTime) / 1000;
                    if (currentTime > timeOut) {
                        log.error("Failed to download expected file");
                        return downloadedFileName;
                    }
                    valid = watchKey.reset();
                }
            } while (valid);
        }

        catch (InterruptedException e) {
            log.error("Interrupted error - {}", e.getMessage());
        } catch (NullPointerException e) {
            log.error("Download operation timed out.. Expected file was not downloaded");
        } catch (Exception e) {
            log.error("Error occured - {}", e.getMessage());
        }
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
        
        boolean flag = false;

        BufferedInputStream file = null;
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        String parsedText = null;

        try {
            file = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            String errorMessage = SentinelStringUtils.format("Could not open the PDF file: {}", url.toString());
            throw new IOException(errorMessage, e);
        } finally {
        	file.close();
        }

        try {
            pdfStripper = new PDFTextStripper();
        } catch (IOException e) {
            String errorMessage = SentinelStringUtils.format("Could not create PDFTextStripper() for PDF file {} Setting -Dssltrust=all on the command line will bypass PKIX errors.", url.toString());
            throw new IOException(errorMessage, e);
        }
        pdfStripper.setStartPage(pageStart);
        pdfStripper.setEndPage(pageEnd);

        try {
            pdDoc = PDDocument.load(file);
        } catch (InvalidPasswordException e) {
            String errorMessage = SentinelStringUtils.format("PDF file {} was password protected.", url.toString());
            throw new IOException(errorMessage, e);
        } catch (IOException e) {
            String errorMessage = SentinelStringUtils.format("Could not load the PDF {}", url.toString());
            throw new IOException(errorMessage, e);
        }
        
        try {
            parsedText = pdfStripper.getText(pdDoc);
        } catch (IOException e) {
            String errorMessage = SentinelStringUtils.format("Could not get text from PDFTextStripper() for PDF file {}", url.toString());
            throw new IOException(errorMessage, e);
        }

        if (pdDoc != null) {
            try {
                pdDoc.close();
            } catch (IOException e) {
                String errorMessage = SentinelStringUtils.format("Could not close the PDF document {}", url.toString());
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
     * @throws IOException Throws an error if the PDF file cannot be opened.
     */
    public static boolean verifyTextInDownloadedPDF(String expectedText, File pdfFile, int pageStart, int pageEnd)
            throws IOException {

        boolean flag = false;

        PDFTextStripper pdfStripper = null;
        FileInputStream fis = null;
        PDDocument pdDoc = null;
        String parsedText = null;

        try {
            fis = new FileInputStream(pdfFile);
            BufferedInputStream file = new BufferedInputStream(fis);

            pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(pageStart);
            pdfStripper.setEndPage(pageEnd);

            pdDoc = PDDocument.load(file);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (IOException e) {
            log.error("Unable to open PDF Parser. " + e.getMessage());
            try {
                if (pdDoc != null)
                    pdDoc.close();
            } catch (Exception e1) {
                log.error(e1);
            }
        } finally {
        	fis.close();
        }

        log.trace("PDF Parsed Text: \n" + parsedText);

        if (parsedText != null && parsedText.contains(expectedText)) {
            flag = true;
        }

        return flag;
    }
    /**
     * Returns the path of an image containing the entire contents of the page using 
     * the index given as the page number to capture
     * 
     * @param index int page index of the page to be rendered
     * @param pdfFile File pdf containing image
     * @return String file location of image.
     * @throws IOException if error durring file io or during document load
     */
    public static String saveImageInPDF(int index, File pdfFile) throws IOException {
        String imageLocation = "test.jpg";
        PDFRenderer pdfRenderer = null;
        BufferedImage image = null;

        PDDocument document = PDDocument.load(pdfFile);

        pdfRenderer = new PDFRenderer(document);
        image = pdfRenderer.renderImage(index);
        ImageIO.write(image, "JPEG", new File(imageLocation));
        document.close();

        return imageLocation;
    }
    /**
     * Returns true if given file is successfully deleted.
     * 
     * @param file File file to delete
     * @return boolean true if file exists and is successfully deleted
     * @throws IOException if there is an error while deleting the file
     */
    public static boolean deleteFile(File file) throws IOException {
        return Files.deleteIfExists(file.toPath());
    }
    /**
     * Returns filename
     * 
     * @return String the filename
     */
    public static String getFilename() {
        return filename;
    }
    /**
     * Sets filename for given file
     * 
     * @param filename String file to set
     */
    public static void setFilename(String filename) {
        DownloadManager.filename = filename;
    }
    /**
     * Returns string of current file extension
     * 
     * @return String file extension
     */
    public static String getFileExtension() {
        return fileExtension;
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
     * Returns file
     * 
     * @return File returns a File object
     */
    public static File getFile() {
        return file;
    }
    /**
     * Sets given file
     * 
     * @param file File file to set
     */
    public static void setFile(File file) {
        DownloadManager.file = file;
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
     * Sets downlaodDirectory upon creation of the Download manager.
     */
    public static String createDownloadDirectory() {
    	String downloadDirectory = ConfigurationManager.getOptionalProperty("downloadDirectory");
    	if (downloadDirectory == null) {
    		downloadDirectory = "../../Downloads";
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
}
