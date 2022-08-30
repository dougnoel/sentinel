package com.dougnoel.sentinel.files;

import com.dougnoel.sentinel.system.DownloadManager;
import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class CsvFileTests {

    private static final String filePathOneHeader = "src/test/resources/csvs/test_1header.csv";
    private static final String filePathZeroHeaders = "src/test/resources/csvs/test_0header.csv";


    @Test(expected = FileNotFoundException.class)
    public void failToCreateCsvFile_BadPath() throws FileNotFoundException {
        new CsvFile(Path.of("path/does/not/exist"));
    }

    @Test
    public void createCsvFiles() throws FileNotFoundException {
        var filePath = Path.of(filePathOneHeader);
        CsvFile file1 = new CsvFile(filePath);
        assertNotNull("Expected new CsvFile to be created.", file1);
        CsvFile file2 = new CsvFile(filePath, CSVFormat.DEFAULT);
        assertNotNull("Expected new CsvFile to be created.", file2);
        CsvFile file3 = new CsvFile(filePath, CSVFormat.EXCEL, 0);
        assertNotNull("Expected new CsvFile to be created.", file3);
        assertEquals("File 1 should equal file 2.", file1, file2);
        assertNotEquals("File 2 should not equal file 3.", file2, file3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void failToGetHeadersFromHeaderlessFile() throws FileNotFoundException {
        (new CsvFile(Path.of(filePathZeroHeaders), 0)).getHeaders();
    }

    @Test
    public void getNumberOfRowsInCsv() throws FileNotFoundException {
        var filePath = Path.of(filePathZeroHeaders);
        CsvFile file = new CsvFile(filePath, 0);
        assertEquals("Expected CSV file to have 4 rows total.", 4, file.getNumberOfRows());
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
        assertEquals("Expected CSV cell in column Age, row 2, to be 3", "3", file.getCellData("age", 2));
    }
}
