@298
Feature: 298 Implement the image-comparison library
As a user of Sentinel, I would to be able to compare two screen shots and determine whether they 
are exactly the same or not, so that I can create tests based on visual UI differences in both 
the web and windows.

  @298A
  Scenario: 298A Compare two identical screenshots
  1. Need to be able to store a unique screenshot that we can reference in a later step for comparison.
	2. We need to have a place to put a screenshot for tests to use for comparison every time it runs. Different from step 1 in that it is not dynamically created every time.
	3. We need to have a way to screenshot an element on a page.
	
    Given I am on the Internet Page
    When I take a screenshot of the header
    Then I verify the header matches the expected image

  @298B
  Scenario: 298B We need to have a way to store screenshots when tests fail showing the differences.
    Given I am on the Dropdown Page
      And I take a screenshot of the dropdown
    When I select the 2nd option from the dropdown 
    Then I verify the dropdown matches the expected image