@element-visibilty
Feature: Validtae the Fucntionality of Elements Existing and Not Existing

  @element-exists @element-does-not-exist @145
  Scenario: Test Link Existance
    Given I am on the Guinea Pig Page
    Then I verify the Example Div exists
    	And I verify the Example Link exists
    	And I verify the Invisible Div does not exist
    	And I verify the Fake Div does not exist