package com.dougnoel.sentinel.files;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class CsvFileTests {

    @Test(expected = FileNotFoundException.class)
    public void failToCreateCsvFile_BadPath() throws FileNotFoundException {
        new CsvFile(Path.of("path/does/not/exist"));
    }

    @Test
    public void createCsvFiles() throws FileNotFoundException {
        var filePath = Path.of("src/test/resources/csvs/test_1header.csv");
        CsvFile file1 = new CsvFile(filePath);
        assertNotNull("Expected new CsvFile to be created.", file1);
        CsvFile file2 = new CsvFile(filePath, CSVFormat.DEFAULT);
        assertNotNull("Expected new CsvFile to be created.", file2);
        CsvFile file3 = new CsvFile(filePath, CSVFormat.EXCEL, 0);
        assertNotNull("Expected new CsvFile to be created.", file3);
        assertTrue("File 1 should equal file 2.", file1.equals(file2));
        assertFalse("File 2 should not equal file 3.", file2.equals(file3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void failToGetHeadersFromHeaderlessFile() throws FileNotFoundException {
        (new CsvFile(Path.of("src/test/resources/csvs/test_0header.csv"), 0)).getHeaders();
    }
}
