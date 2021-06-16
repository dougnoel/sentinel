#language: en
#Author: Shobana Namburaj
@197
Feature: Heroku App Mouse Hover
  Login to the HerokuApp 
  And click on the Hovers
  And Mouse Hover on User Profile
  And verify the tooltip contains the text

  @197A
  Scenario: Successfully was able to Mouse Over on User1, User2, User3
    Given I am on the Internet Herokuapp Page
    When I click the hovers link   
    Then I am redirected to the Hovers Page
     And I mouse over User Profile
    Then I verify the tooltip contains the text "name: user1"  