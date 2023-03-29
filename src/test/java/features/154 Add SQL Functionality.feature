@154 @sql
Feature: 154 Add SQL Functionality

	@query
  Scenario: Query Database
    Given I connect to the Local Host MySQL Database
      And I use the test_db database as testuser
    When I submit the query
    	"""
    	SELECT tutorial_title
    	FROM fake_data
    	WHERE tutorial_author='John Poul';
    	"""
   Then I verify the query result does not contain the text lorem