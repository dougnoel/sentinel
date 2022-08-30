package com.dougnoel.sentinel.files;

import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.DownloadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public abstract class TestFile extends File{
    protected static final Logger log = LogManager.getLogger(TestFile.class.getName()); // Create a logger.

    /**
     * Creates a TestFile from the DownloadManager's latest download and immediately checks that it exists. If it doesn't, an exception is thrown.
     * @throws FileNotFoundException in the case that a file is not found at the path given by the DownloadManager,
     * or if a file has not been downloaded in this test session.
     */
    protected TestFile() throws FileNotFoundException {
        this(DownloadManager.getMostRecentDownloadPath());
    }

    /**
     * Creates a TestFile and immediately checks that it exists. If it doesn't, an exception is thrown.
     * @param pathToFile Path the path to the file to test.
     * @throws FileNotFoundException in the case that a file is not found at the given path.
     */
    protected TestFile(Path pathToFile) throws FileNotFoundException {
        super(pathToFile.toString());
        if(!exists()){
            throw new FileNotFoundException(SentinelStringUtils.format("File at {} not found. Cannot continue tests using this filepath.", toPath()));
        }
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()){
            return false;
        }
        return super.equals(obj);
    }
}
