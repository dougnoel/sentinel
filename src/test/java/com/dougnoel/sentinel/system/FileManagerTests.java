package com.dougnoel.sentinel.system;

import com.dougnoel.sentinel.strings.SentinelStringUtils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileManagerTests {
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
}