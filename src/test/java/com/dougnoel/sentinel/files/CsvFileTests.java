package com.dougnoel.sentinel.files;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;

public class CsvFileTests {

    @Test(expected = FileNotFoundException.class)
    public void failToCreateCsvFile_BadPath() throws FileNotFoundException {
        new CsvFile(Path.of("path/does/not/exist"));
    }

    @Test
    public void createCsvFiles() throws FileNotFoundException {
        var filePath = Path.of("src/test/resources/csvs/test_1header.csv");
        assertNotNull("Expected new CsvFile to be created.", new CsvFile(filePath));
        assertNotNull("Expected new CsvFile to be created.", new CsvFile(filePath, CSVFormat.EXCEL));
        assertNotNull("Expected new CsvFile to be created.", new CsvFile(filePath, CSVFormat.EXCEL, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void failToGetHeadersFromHeaderlessFile() throws FileNotFoundException {
        (new CsvFile(Path.of("src/test/resources/csvs/test_0header.csv"), 0)).getHeaders();
    }
}
