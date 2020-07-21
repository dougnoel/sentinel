@154 @sql
Feature: 154 Add SQL Functionality

	@query
  Scenario: Query Database
    Given I connect to the Local Host MySQL Database
#    	And I use the test_db database   	
    When I submit the query
    	"""
    	SELECT title
    	FROM fake_data
    	WHERE author='John Poul';
    	"""
#    Then I should get the query result "Learn PHP"

  @update
  Scenario: Update Database
    Given I connect to the Local Host MySQL Database
    	And I use the test_db database   	
    When I run the update "update fake_data set title='JAVA Tutorial' where id=3;"
    Then I should get a success message 