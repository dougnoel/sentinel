package com.dougnoel.sentinel.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class ZipFile extends TestFile {

    /**
     * Creates new ZipFile from the most recently-downloaded path.
     * @throws FileNotFoundException In the case that the most recently downloaded file does not exist.
     */
    public ZipFile() throws FileNotFoundException {
        super();
    }

    /**
     * Creates new ZipFile from the specified path.
     * @param pathToFile Path path to the file to create as ZipFile
     * @throws FileNotFoundException In the case that the specified file does not exist.
     */
    public ZipFile(Path pathToFile) throws FileNotFoundException {
        super(pathToFile);
    }

    /**
     * Gets a List of all file names within the zip. Can find files in subdirectories within the zip, but not within nested zips.
     * @return List&lt;String&gt; list of all file names within the zip.
     * @throws IOException In the case that the file cannot be opened as a zip, or a generic IOException occurs.
     */
    public List<String> getFileNames() throws IOException {
        try(var zip = new java.util.zip.ZipFile(this)) {
            return zip.stream().map(ZipEntry::getName).collect(Collectors.toList());
        }
    }
}
