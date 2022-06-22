package com.dougnoel.sentinel.system;

import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileManagerTests {
    private static final String LINUX = "linux";
    private static final String MAC = "mac";
    private static final String REAL_OS = System.getProperty("os.name");

    @After
    public void tearDownAfterEachTest() {
        System.setProperty("os.name", REAL_OS);
    }

    @Test
    public void testSpecialCharacterReplacement() {
        String testString = "~`!@#$%^&()=_+[]{}\\\\|:\\\";'<>,?/";
        String resultString = FileManager.sanitizeString(testString);
        String resultMessage = SentinelStringUtils.format("The test string {} was expected to only consist of '_' characters. Instead {} was returned.", testString, resultString);

        assertTrue(resultMessage, resultString.matches("^_+$"));
    }

    @Test
    public void testNonSpecialCharactersUntouched() {
        String testString = "ABDCEFGHIJKL12345-.67890MNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy";
        String resultString = FileManager.sanitizeString(testString);
        String resultMessage = SentinelStringUtils.format("The test string should not have been modified from {}, but was returned as {}", testString, resultString);

        assertEquals(resultMessage, testString, resultString);
    }

    @Test
    public void testPathSeparatorReplacement() {
        String testPathString = "test/this\\\\now\\please";
        String resultPathString = FileManager.convertPathSeparators(testPathString);
        String resultMessage = SentinelStringUtils.format("The test path should not have contained '/', and should have contained File.seperator entries. Instead the path was {}", resultPathString);

        assertTrue(resultMessage, resultPathString.contains(File.separator) && !resultPathString.contains("/"));
    }

    @Test
    public void localAppDataTranslation() {
        String start = FileManager.convertPathSeparators("C:/Users/");
        String end = FileManager.convertPathSeparators("/AppData/Local");

        String translatedLocalAppData = FileManager.winSpecialFolderConverter("%localAppData%");

        String resultMessage = SentinelStringUtils.format("The %localappdata% shortcut should have translated to begin with '{}' and end with '{}', but was '{}'", start, end, translatedLocalAppData);
        assertTrue(resultMessage, translatedLocalAppData.startsWith(start) && translatedLocalAppData.endsWith(end));
    }

    @Test
    public void appDataTranslation() {
        String start = FileManager.convertPathSeparators("C:/Users/");
        String end = FileManager.convertPathSeparators("/AppData/Roaming");

        String translatedAppData = FileManager.winSpecialFolderConverter("%appdata%");

        String resultMessage = SentinelStringUtils.format("The %localappdata% shortcut should have translated to begin with '{}' and end with '{}', but was '{}'", start, end, translatedAppData);
        assertTrue(resultMessage, translatedAppData.startsWith(start) && translatedAppData.endsWith(end));
    }

    @Test
    public void userProfileTranslation() {
        String start = FileManager.convertPathSeparators("C:/Users/");

        String translatedUserProfile = FileManager.winSpecialFolderConverter("%appdata%");
        String resultMessage = SentinelStringUtils.format("The %localappdata% shortcut should have translated to begin with '{}', but was '{}'", start, translatedUserProfile);
        assertTrue(resultMessage, translatedUserProfile.startsWith(start));
    }

    @Test(expected = FileException.class)
    public void linuxWindowsFolderTranslationException() {
        System.setProperty("os.name", LINUX);
        FileManager.winSpecialFolderConverter("%appdata%");
    }

    @Test(expected = FileException.class)
    public void macWindowsFolderTranslationException() {
        System.setProperty("os.name", MAC);
        FileManager.winSpecialFolderConverter("%localappdata%");
    }
}