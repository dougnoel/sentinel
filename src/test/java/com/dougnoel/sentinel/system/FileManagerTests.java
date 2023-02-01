package com.dougnoel.sentinel.system;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.Before;

import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class FileManagerTests {
    private static final String LINUX = "linux";
    private static final String MAC = "mac";
    private static final String WINDOWS = "windows";
    private static final String OS_NAME = "os";

    @Before(value="@WindowsOnly")
    public static void before() {
        assumeTrue(Configuration.operatingSystem().contentEquals(WINDOWS));
    }
    
    @After
    public void tearDownAfterEachTest() {
        Configuration.clear(OS_NAME);
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
		if (Configuration.operatingSystem().contentEquals(WINDOWS)) {
			String testPathString = "test/this\\\\now\\please";
			String resultPathString = FileManager.convertPathSeparators(testPathString);
			String resultMessage = SentinelStringUtils.format(
					"The test path should not have contained '/', and should have contained File.seperator entries. Instead the path was {}",
					resultPathString);

			assertTrue(resultMessage, resultPathString.contains(File.separator) && !resultPathString.contains("/"));
		} else
			assertTrue(true);
    }

	@Test
	public void localAppDataTranslation() {
		if (Configuration.operatingSystem().contentEquals(WINDOWS)) {
			String start = FileManager.convertPathSeparators("C:/Users/");
			String end = FileManager.convertPathSeparators("/AppData/Local");

			String translatedLocalAppData = FileManager.winSpecialFolderConverter("%localAppData%");

			String resultMessage = SentinelStringUtils.format(
					"The %localappdata% shortcut should have translated to begin with '{}' and end with '{}', but was '{}'",
					start, end, translatedLocalAppData);
			assertTrue(resultMessage, translatedLocalAppData.startsWith(start) && translatedLocalAppData.endsWith(end));
		} else
			assertTrue(true);
	}

	@Test
	public void appDataTranslation() {
		if (Configuration.operatingSystem().contentEquals(WINDOWS)) {
			String start = FileManager.convertPathSeparators("C:/Users/");
			String end = FileManager.convertPathSeparators("/AppData/Roaming");

			String translatedAppData = FileManager.winSpecialFolderConverter("%appdata%");

			String resultMessage = SentinelStringUtils.format(
					"The %localappdata% shortcut should have translated to begin with '{}' and end with '{}', but was '{}'",
					start, end, translatedAppData);
			assertTrue(resultMessage, translatedAppData.startsWith(start) && translatedAppData.endsWith(end));
		} else
			assertTrue(true);
	}

    @Test
	public void userProfileTranslation() {
		if (Configuration.operatingSystem().contentEquals(WINDOWS)) {
			String start = FileManager.convertPathSeparators("C:/Users/");

			String translatedUserProfile = FileManager.winSpecialFolderConverter("%appdata%");
			String resultMessage = SentinelStringUtils.format(
					"The %localappdata% shortcut should have translated to begin with '{}', but was '{}'", start,
					translatedUserProfile);
			assertTrue(resultMessage, translatedUserProfile.startsWith(start));
		} else
			assertTrue(true);
	}

    @Test(expected = FileException.class)
    public void linuxWindowsFolderTranslationException() {
    	Configuration.update(OS_NAME, LINUX);
        FileManager.winSpecialFolderConverter("%appdata%");
    }

    @Test(expected = FileException.class)
    public void macWindowsFolderTranslationException() {
    	Configuration.update(OS_NAME, MAC);
        FileManager.winSpecialFolderConverter("%localappdata%");
    }
}