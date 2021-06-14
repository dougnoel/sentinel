#language: en
#Author: Shobana Namburaj
@197
Feature: Heroku App Mouse Hover
  Login to the HerokuApp 
  And click on the Hovers
  And Mouse Hover on User1 and validate the tooltip text

  @197A
  Scenario: Successfully was able to Mouse Over and validate the text "name: user1"
    Given I am on the Internet Herokuapp Page
     When I click the hovers link   
     Then I am redirected to the Hovers Page
     When I mouse over User Div
     Then I wait 3 seconds
      And I verify the tooltip contains the text "name: user1"  