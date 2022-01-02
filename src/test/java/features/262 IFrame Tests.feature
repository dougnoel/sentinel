#Author: Doug Noël
@262 @example @iframe
Feature: IFrame Tests
  Testing that elements can be located within IFrames and nested IFrames.

  @262A
  Scenario: 262A IFrame Tests
    Given I am on the IFrame Page
    When I click the Male Radio Button
    Then I verify the Example Table contains the Name column
      And I verify the Male Radio Button is checked
      And I verify the IFrame Header exists