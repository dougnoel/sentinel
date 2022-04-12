package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import com.dougnoel.sentinel.system.DownloadManager;
import com.dougnoel.sentinel.webdrivers.Driver;

import io.cucumber.java.en.Then;

public class PDFVerificationSteps {

	/**
     * Validates whether the currently open pdf contains the given text on the page(s) passed to it.
     * If a page number is passed for the first page and a null is passed for the second page, then 
     * only that page is searched. If two page numbers are passed, then the entire range is searched,
     * inclusive of the two page numbers given.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>	Then I see the text Validation Text appears on the 3rd page of the pdf</li>
     * <li>	Then I see the text 1Q2W3E_4R5T appears between the 1st and 4th pages of the pdf</li>
     * </ul>
     * <b>NOTE:</b> This will fail if the browser window is not currently open with a pdf loaded.
     * Should be executed after the i_open_the_document_with_extension_in_a_new_tab(linkName,extension) method
     * is called. The cucumber step is: @When("^I open the (.*) (pdf) in a new tab$")
     * <p>
     * @param text_to_verify String the text string expected to appear in the currently open PDF file
     * @param firstPageNumber int the page to check or the first page in the range if the second value is not null
     * @param lastPageNumber Integer the last page in the range; indicates to only check one page if set to null
     * @throws Throwable if any errors are raised they will fail the current test
     */
    @Then("^I see the text (.*) appears (?:on|between) the (\\d+)(?:st|nd|rd|th)(?: and )?(\\d+)?(?:st|nd|rd|th)? pages? of the pdf$")
    public static void i_see_the_text_appears_on_pages_of_the_pdf(String text_to_verify, int firstPageNumber, Integer lastPageNumber) throws Throwable {
            URL url = new URL(Driver.getCurrentUrl());
            assertTrue(url.toString().contains(".pdf"));
            if (lastPageNumber == null) {
            	assertTrue(DownloadManager.verifyPDFContent(url, text_to_verify, firstPageNumber));	
            } else {
            	assertTrue(DownloadManager.verifyPDFContent(url, text_to_verify, firstPageNumber, lastPageNumber));
            }
    }
    
    
}
