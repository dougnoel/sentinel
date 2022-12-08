#language: en
@89
Feature: 89 API Testing
  Tests using the Swagger Pet Store example API.
  https://petstore3.swagger.io/
      
  @89A
  Scenario: 89A POST Swagger Test
    Given I use the API named Pet Store API
    When I load puppydata to use as the API body
      And I send a POST request to the pet endpoint
    Then I verify the response code equals 200
      And I validate the response contains the text "doggie"
      
  @89B
  Scenario: 89B GET Swagger Test
    Given I use the API named Pet Store API
    When I GET record 10 from the pet endpoint
    Then I verify the response code equals 200
      And I validate the response contains the text "photoUrls"
      
  @89C
  Scenario: 89C PUT Swagger Test
    Given I use the API named Pet Store API
    When I set the API body to
    """
    {
	  "id": 10,
	  "name": "puppy",
	  "category": {
	    "id": 1,
	    "name": "Dogs"
	  },
	  "photoUrls": [
	    "string"
	  ],
	  "tags": [
	    {
	      "id": 0,
	      "name": "string"
	    }
	  ],
	  "status": "available"
	}
    """
    And I send a PUT request to the pet endpoint
    Then I verify the response code equals 200
      And I validate the response contains the text "puppy"
      
  @89D
  Scenario: 89D Parameter Swagger Test
    Given I use the API named Pet Store API
      And I add a status parameter with the value available
    When I send a GET request to the pet/findByStatus endpoint
    Then I verify the response code equals 200
      And I validate the response contains the text "photoUrls"
      
  @89E
  Scenario: 89E DELETE Swagger Test
    Given I use the API named Pet Store API
    When I DELETE record 10 from the pet endpoint
    Then I verify the response code equals 200
    When I GET record 10 from the pet endpoint
    Then I verify the response code equals 404
  