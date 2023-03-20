#language: en
#Author: Shobana Namburaj
@197
Feature: 197 Heroku App Mouse Hover
  Login to the HerokuApp 
  And click on the Hovers
  And I hover on User Profile
  And I verify the text "Name: User1" is visible

  @197A
  Scenario: Successfully was able to Mouse Over and validate the text "name: user1"
    Given I am on the Internet Page
    When I click the hovers link   
    Then I am redirected to the Hovers Page
     And I verify the Mouse Over tooltip has the value "name: user1"
     And I verify the Mouse Over tooltip does not have the value "blah" 
