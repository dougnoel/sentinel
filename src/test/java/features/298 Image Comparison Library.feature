@298
Feature: 298 Implement the image-comparison library
As a user of Sentinel, I would to be able to compare two screen shots and determine whether they 
are exactly the same or not, so that I can create tests based on visual UI differences in both 
the web and windows.

  @298A
  Scenario: 298A Compare two unalike screenshots of an element first small second larger
  Given I am on the Add Remove Elements Page
    And I take a screenshot of the Page Contents
  When I click the Add Element Button
    And I verify the Delete Element Button exists
  Then I verify the Page Contents do not match the original image
    
  @298B
  Scenario: 298B Compare two unalike screenshots of an element first large second smaller
  Given I am on the Add Remove Elements Page
    And I click the Add Element Button
    And I verify the Delete Element Button exists
    And I take a screenshot of the Page Contents
  When I click the Delete Element Button
  Then I verify the Page Contents do not match the original image
    
  @298C
  Scenario: 298C Compare two unalike screenshots of a page first large second smaller
  Given I am on the Add Remove Elements Page
    And I click the Add Element Button
    And I verify the Delete Element Button exists
    And I take a screenshot of the page
  When I click the Delete Element Button
  Then I verify the page does not match the original image

  @298D
  Scenario: 298D Compare two unalike screenshots of the same size
  Given I am on the Dropdown Page
    And I take a screenshot of the dropdown
  When I select the 2nd option from the dropdown 
  Then I verify the dropdown does not match the original image
    
  @298E
  Scenario: 298E Compare two identical screenshots of an element of the same size after an operation
  Given I am on the Dropdown Page
    And I select the 2nd option from the dropdown
    And I take a screenshot of the dropdown
    And I select the 1st option from the dropdown
  When I select the 2nd option from the dropdown 
  Then I verify the dropdown matches the expected image
    
  @298F
  Scenario: 298F Compare two identical screenshots of a page of the same size after an operation
  Given I am on the Add Remove Elements Page
    And I take a screenshot of the page
    And I click the Add Element Button
  When I click the Delete Element Button
  Then I verify the page matches the expected image