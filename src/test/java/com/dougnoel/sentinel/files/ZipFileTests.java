package com.dougnoel.sentinel.files;

import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.system.DownloadManager;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class ZipFileTests {

    private static final String zipFilePath = "src/test/resources/zips/testzipwithsubfolder.zip";

    @BeforeClass
    public static void setUp() throws IOException {
        DownloadManager.clearDownloadDirectory();
    }

    @After
    public void tearDown(){
        Driver.quitAllDrivers();
    }

    @Test(expected = FileNotFoundException.class)
    public void failToCreateZipFile_BadPath() throws FileNotFoundException {
        new ZipFile(Path.of("path/does/not/exist"));
    }

    @Test
    public void createZipFile() throws FileNotFoundException {
        var filePath = Path.of(zipFilePath);
        ZipFile file = new ZipFile(filePath);
        assertNotNull("Expected new CsvFile to be created.", file);
    }

    @Test
    public void createZipFileFromDownload() throws IOException, InterruptedException {
        BaseSteps.navigateToPage("RadioButtonPage");
        BaseSteps.click("zip_download_link");
        DownloadManager.setFileExtension("zip");
        String filename = DownloadManager.monitorDownload();
        ZipFile file = new ZipFile();
        assertEquals("Expected filename of created Zip object to match downloaded filename.", filename, file.getName());
    }
}
