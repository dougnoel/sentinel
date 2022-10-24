package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Time;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.assertTrue;
import org.apache.commons.lang3.StringUtils;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WindowAndTabVerificationSteps {
    private static final Logger log = LogManager.getLogger(TextVerificationSteps.class.getName()); // Create a logger.

    /**
     * Checks if the current tab or window either does or does not equal or contain the passed text
     * @param assertion String whether the title should or should not have the text
     * @param matchType String whether the title should exactly equal or contains
     * @param titleText String the title text to verify
     */
    @Then("I verify the (?:tab|window)( does not)? (has|have|contains?) the title text \"([^\"]*)\"$")
    public static void VerifyTitle(String assertion, String matchType, String titleText) {
        ExpectedCondition<Boolean> condition;

        boolean negate = !StringUtils.isEmpty(assertion);
        String negateText = negate ? "not " : "";
        boolean partialMatch = matchType.contains("contain");
        String partialMatchText = partialMatch ? "contain" : "exactly match";

        Boolean result;

        if (partialMatch)
            condition = ExpectedConditions.titleContains(titleText);
        else
            condition = ExpectedConditions.titleIs(titleText);

        if (negate)
            condition = ExpectedConditions.not(condition);

        try {
            result = new WebDriverWait(Driver.getWebDriver(), Time.out().toSeconds(), Time.interval().toMillis())
                    .ignoring(StaleElementReferenceException.class)
                    .until(condition);
        }
        catch (TimeoutException e) {
            result = false;
        }

        var expectedResult = SentinelStringUtils.format(
                "Expected the title to {}{} the text {}. The title contained the text: {}",
                negateText, partialMatchText, titleText, Driver.getTitle()
                        .replace("\n", " "));
        log.trace(expectedResult);
        assertTrue(expectedResult, result);
    }
}
