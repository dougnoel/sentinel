package com.dougnoel.sentinel.files;

import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.system.DownloadManager;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.apache.commons.csv.CSVFormat;
import org.junit.After;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class CsvFileTests {

    private static final String filePathOneHeader = "src/test/resources/csv/test_1header.csv";
    private static final String filePathZeroHeaders = "src/test/resources/csv/test_0header.csv";


    @After
    public void tearDown(){
        Driver.quitAllDrivers();
    }

    @Test(expected = FileNotFoundException.class)
    public void failToCreateCsvFile_BadPath() throws FileNotFoundException {
        new CsvFile(Path.of("path/does/not/exist"));
    }

    @Test
    public void createCsvFile_0() throws FileNotFoundException {
        var filePath = Path.of(filePathOneHeader);
        CsvFile file = new CsvFile(filePath);
        assertNotNull("Expected new CsvFile to be created.", file);
    }

    @Test
    public void createCsvFile_1() throws FileNotFoundException {
        var filePath = Path.of(filePathOneHeader);
        CsvFile file = new CsvFile(filePath, CSVFormat.DEFAULT);
        assertNotNull("Expected new CsvFile to be created.", file);
    }

    @Test
    public void createCsvFile_2() throws FileNotFoundException {
        var filePath = Path.of(filePathOneHeader);
        CsvFile file = new CsvFile(filePath, CSVFormat.EXCEL, 0);
        assertNotNull("Expected new CsvFile to be created.", file);
    }

    @Test
    public void createCsvFileFromDownload() throws IOException, InterruptedException {
        BaseSteps.navigateToPage("DownloadsTestPage");
        BaseSteps.click("sample_download_link");
        String filename = DownloadManager.monitorDownload();
        CsvFile file = new CsvFile();
        assertEquals("Expected filename of created CSV object to match downloaded filename.", filename, file.getName());
    }

    @Test
    public void compareCsvFiles_Equal() throws FileNotFoundException {
        var filePath = Path.of(filePathOneHeader);
        CsvFile file1 = new CsvFile(filePath);
        CsvFile file2 = new CsvFile(filePath, CSVFormat.DEFAULT);
        assertEquals("File 1 should equal file 2.", file1, file2);
    }
    @Test
    public void compareCsvFiles_Unequal() throws FileNotFoundException {
        var filePath = Path.of(filePathOneHeader);
        CsvFile file1 = new CsvFile(filePath, CSVFormat.DEFAULT);
        CsvFile file2 = new CsvFile(filePath, CSVFormat.EXCEL, 0);
        assertNotEquals("File 1 should not equal file 2.", file1, file2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void failToGetHeadersFromHeaderlessFile() throws FileNotFoundException {
        (new CsvFile(Path.of(filePathZeroHeaders), 0)).readHeaders();
    }

    @Test
    public void getNumberOfRowsInCsv() throws FileNotFoundException {
        var filePath = Path.of(filePathZeroHeaders);
        CsvFile file = new CsvFile(filePath, 0);
        assertEquals("Expected CSV file to have 4 rows total.", 4, file.getNumberOfTotalRows());
    }

    @Test
    public void getHashCode() throws FileNotFoundException{
        var filePath = Path.of(filePathZeroHeaders);
        CsvFile file = new CsvFile(filePath, 0);
        assertNotEquals("Expected CSV hash to be non-zero.", 0, file.hashCode());
    }

    @Test
    public void getCellData() throws FileNotFoundException{
        var filePath = Path.of(filePathOneHeader);
        CsvFile file = new CsvFile(filePath, 1);
        assertEquals("Expected CSV cell in column Age, row 2, to be 3", "3", file.readCellData("age", 2));
    }

    @Test
    public void deleteCellsInRow() throws IOException, InterruptedException {
        BaseSteps.navigateToPage("DownloadsTestPage");
        BaseSteps.click("csv_download_link");
        DownloadManager.monitorDownload(DownloadManager.getDownloadDirectory(), "csv", null);
        Path filePath = DownloadManager.getMostRecentDownloadPath();
        CsvFile file = new CsvFile(filePath, 1);
        file.deleteCellsInRow(1);

        assertEquals("Expected CSV file to have 3 data rows total.", 3, file.getNumberOfDataRows());
    }

    @Test
    public void writeCellInColumnNameRow() throws IOException, InterruptedException {
        BaseSteps.navigateToPage("DownloadsTestPage");
        BaseSteps.click("csv_download_link");
        DownloadManager.monitorDownload(DownloadManager.getDownloadDirectory(), "csv", null);
        Path filePath = DownloadManager.getMostRecentDownloadPath();
        CsvFile file = new CsvFile(filePath, 1);

        file.writeAllCellsInColumn("comment", "test");
        file.writeCellInColumnRow("comment", 2, "me");

        assertNotNull("Expected the CSV file to have edited one cell to 'me'.", file.verifyCellDataContains(2, "comment", "me", false));
    }

    @Test
    public void writeCellInColumnRow() throws IOException, InterruptedException {
        BaseSteps.navigateToPage("DownloadsTestPage");
        BaseSteps.click("csv_download_link");
        DownloadManager.monitorDownload(DownloadManager.getDownloadDirectory(), "csv", null);
        Path filePath = DownloadManager.getMostRecentDownloadPath();
        CsvFile file = new CsvFile(filePath, 1);

        file.writeAllCellsInColumn(5, "test");
        file.writeCellInColumnRow(5, 2, "me");

        assertNotNull("Expected the CSV file to have edited one cell to 'me'.", file.verifyCellDataContains(2, "comment", "me", false));
    }
}