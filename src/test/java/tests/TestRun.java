package tests;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.cucumber.listener.Reporter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import sentinel.exceptions.ConfigurationMappingException;
import sentinel.exceptions.ConfigurationParseException;
import sentinel.exceptions.FileNotFoundException;
import sentinel.exceptions.MissingConfigurationException;
import sentinel.exceptions.SentinelException;
import sentinel.exceptions.WebDriverException;
import sentinel.pages.PageManager;
import sentinel.utils.K;
import sentinel.utils.WebDriverFactory;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, features = "src/test/java/features", glue = { "stepdefinitions",
        "sentinel.steps" }, plugin = {
                "com.cucumber.listener.ExtentCucumberFormatter:reports/extent-cucumber-report.html" }
         , tags = { "@example" }
)

public class TestRun {
    private static final Logger log = LogManager.getLogger(TestRun.class); // Create a logger.
    
    @BeforeClass
    public static void setUpBeforeClass() throws IOException, SentinelException {
    	log.debug("Start");
        // System.setProperty("cucumber.options", "@example");
        System.setProperty(K.ENV, K.DEV); // Set the environment (dev/qa/stage/prod/etc)
        System.setProperty("pageObjectPackages", "pages,apis");
        System.setProperty("os", "Windows");
        System.setProperty("browser", "ie");
//        System.setProperty("saucelabs", "username:saucelabs_key");
        WebDriverFactory.instantiateWebDriver(); 
    }

    @AfterClass
    public static void tearDownAfterClass() throws MissingConfigurationException, ConfigurationParseException, ConfigurationMappingException, WebDriverException, IOException, sentinel.exceptions.IOException, FileNotFoundException {
        log.debug("Driver: {}", WebDriverFactory.getWebDriver());
        PageManager.quit();
        Reporter.loadXMLConfig(new File("conf/extent-config.xml"));
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", System.getProperty("os"));
        Reporter.setTestRunnerOutput("Sample test runner output message");
      
    }
}
