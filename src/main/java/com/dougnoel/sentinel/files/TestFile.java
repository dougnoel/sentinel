package com.dougnoel.sentinel.files;

import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.DownloadManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@SuppressWarnings("serial")
public abstract class TestFile extends File{
    /**
     * Creates a TestFile from the DownloadManager's latest download and immediately checks that it exists. If it doesn't, an exception is thrown.
     *
     * @throws FileNotFoundException in the case that a file is not found at the path given by the DownloadManager,
     * or if a file has not been downloaded in this test session.
     */
    protected TestFile() throws FileNotFoundException {
        this(DownloadManager.getMostRecentDownloadPath());
    }

    /**
     * Creates a TestFile and immediately checks that it exists. If it doesn't, an exception is thrown.
     *
     * @param pathToFile Path the path to the file to test.
     * @throws FileNotFoundException in the case that a file is not found at the given path.
     */
    protected TestFile(Path pathToFile) throws FileNotFoundException {
        super(pathToFile.toString());
        if(!exists()){
            throw new FileNotFoundException(SentinelStringUtils.format("File at {} not found. Cannot continue tests using this filepath.", toPath()));
        }
    }

    /**
     * Returns all text from the file in a single String. Uses UTF-8 encoding.
     * @return String all file contents
     * @throws IOException in the case that the file does not exist or cannot be read, or another generic IO error.
     */
    public String getAllText() throws IOException {
        return FileUtils.readFileToString(this, StandardCharsets.UTF_8);
    }

}
