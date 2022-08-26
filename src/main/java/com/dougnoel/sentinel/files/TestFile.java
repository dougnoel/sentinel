package com.dougnoel.sentinel.files;

import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.DownloadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.nio.file.Path;

abstract public class TestFile {
    protected Path filePath;

    protected static final Logger log = LogManager.getLogger(Element.class.getName()); // Create a logger.

    public TestFile() throws FileNotFoundException {
        this(DownloadManager.getMostRecentDownloadPath());
    }

    public TestFile(Path pathToFile) throws FileNotFoundException {
        filePath = pathToFile;
        if(!exists()){
            throw new FileNotFoundException(SentinelStringUtils.format("File at {} not found. Cannot continue tests using this filepath.", filePath));
        }
    }

    public boolean exists(){
        return filePath.toFile().exists();
    }
}
