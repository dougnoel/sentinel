#language: en
@89
Feature: 89 API Testing
  Tests using the Swagger Pet Store example API.
  https://petstore3.swagger.io/
      
  @89A
  Scenario: 89A POST Swagger Test
    Given I use the API named Pet Store API
    When I load puppydata to use as the request body
      And I send a POST request to the pet endpoint
    Then I verify the response code equals 200
      And I verify the response was received in less than 2 seconds
      And I validate the response contains the text "doggie"
      
  @89B
  Scenario: 89B GET Swagger Test
    Given I use the API named Pet Store API
    When I GET record {test_id} from the pet endpoint
    Then I verify the response code equals 200
      And I verify the response was received in less than 0.8 seconds
      And I validate the response contains the text "photoUrls"
      
  @89C
  Scenario: 89C PUT Swagger Test
    Given I use the API named Pet Store API
    When I set the request body to
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

  @89F
  Scenario: 89F DELETE Header Swagger Test
    Given I use the API named Pet Store API
    When I add an api_key header with the value 123
    When I DELETE record 10 from the pet endpoint
    Then I verify the response code equals 200
    When I GET record 10 from the pet endpoint
    Then I verify the response code equals 404

  @89G
  Scenario: 89G Body With Parameters Test
    Given I use the API named Pet Store API
    When I initialize the configuration values as follows
    """
    id: 10
    category_name: puppies
    """
    When I set the request body to
    """
    {
	  "id": {id},
	  "name": "puppy",
	  "category": {
	    "id": 1,
	    "name": "{category_name}"
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
    And I send a POST request to the pet endpoint
    Then I verify the response code equals 200
    And I validate the response contains the text "puppy"

  @89H
  Scenario: 89H URL With Parameter Test
    Given I use the API named Pet Store API
    When I initialize the configuration values as follows
    """
    id: 10
    """
      And I send a GET request to the pet/{id} endpoint
    Then I verify the response code equals 200

  @89I
  Scenario: 89I Query String Stored Parameter Test
    Given I use the API named Pet Store API
    When I initialize the configuration values as follows
    """
    dog_status: sold
    """
      And I add a status parameter with the value {dog_status}
    When I send a GET request to the pet/findByStatus endpoint
    Then I verify the response code equals 200
      And I validate the response contains the text "sold"

  @89J @513
  Scenario: 89J / 513 Attempt to send multipart/form-data request and observe bad request response
    Given I use the API named Pet Store API
    When I set the request body to upload a file from the location src/test/java/images/eclipse_tool_bar_icon_run.png as a multipart/form-data with the name file
      And I add an additionalMetadata parameter with the value {"source": "sentinel"}
      And I initialize the configuration values as follows
    """
    id: 10
    """
      And I send a POST request to the pet/{id}/uploadImage endpoint
    Then I verify the response code equals 415