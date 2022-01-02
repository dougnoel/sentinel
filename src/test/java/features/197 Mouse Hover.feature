#language: en
#Author: Shobana Namburaj
@197
Feature: 197 Heroku App Mouse Hover
  Login to the HerokuApp 
  And click on the Hovers
  And Mouse Hover on User Profile and validate the text

  @197A
  Scenario: Successfully was able to Mouse Over and validate the text "name: user1"
    Given I am on the Internet Page
    When I click the hovers link   
    Then I am redirected to the Hovers Page       
     And I verify the Mouse Over has the value "name: user1"
     And I verify the Mouse Over does not have the value "blah" 
