package steps;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.dougnoel.sentinel.steps.AccountSteps;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.steps.WindowAndTabSteps;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.Given;

public class SwagLabsSteps {
	@Given("I login to the Sauce Demo Login Page as {}")
	public void i_login_to_the_Sauce_Demo_Login_Page_as_user(String account) throws Throwable {
	    // Given I am on the Sauce Demo Login Page
		ExtentCucumberAdapter.addTestStepLog("And I navigate to the Sauce Demo Login Page");
		BaseSteps.navigateToPage("Sauce Demo Login Page");
	  	// And I fill the account information for account StandardUser into the Username field and the Password field
		ExtentCucumberAdapter.addTestStepLog(
				SentinelStringUtils.format("And I fill the account information for account {} into the Username field and the Password field", 
						account));
		AccountSteps.fillAccountInfoIntoUsernameAndPasswordFields(account, "Username field", "Password field");
	  	// And I click the Login Button
		ExtentCucumberAdapter.addTestStepLog("And I click the Login Button");
		BaseSteps.click("Login button");
	  	// And I am redirected to the Sauce Demo Main Page
		ExtentCucumberAdapter.addTestStepLog("And I am redirected to the Sauce Demo Main Page");
		WindowAndTabSteps.switchTo("Sauce Demo Main Page");
	}
}
