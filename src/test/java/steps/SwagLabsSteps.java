package steps;

import com.dougnoel.sentinel.steps.AccountSteps;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.steps.VerificationSteps;

import io.cucumber.java.en.Given;

public class SwagLabsSteps {
	@Given("I login to the {} Page as {}")
	public void i_login_to_the_Page_as_user(String pageName, String account) throws Throwable {
	    // Given I am on the Sauce Demo Login Page
		BaseSteps.naviagteToPage(pageName);
	  	// And I fill the account information for account StandardUser into the Username field and the Password field
		AccountSteps.fillAccountInfoIntoUsernameAndPasswordFields(account, "Username field", "Password field");
	  	// And I click the Login Button
		BaseSteps.click("Login button");
	  	// And I am redirected to the Sauce Demo Main Page
		VerificationSteps.redirectedToPage("Sauce Demo Main");
	}
}
