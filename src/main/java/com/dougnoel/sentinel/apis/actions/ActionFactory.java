package com.dougnoel.sentinel.apis.actions;

import java.util.Map;
import com.dougnoel.sentinel.apis.API;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.NoSuchActionException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

/**
 * An implementation of the Factory design pattern for creating APi actions.
 * 
 * @author dougnoel@gmail.com
 *
 */
public class ActionFactory {

    private ActionFactory(){
        // Exists to defeat instantiation.
    }

    /**
     * Returns an Object that is an Action using the action name and current active API.
     * 
     * @param actionName String the name of the action to create
     * @param api API the api object that contains the action definition
     * @return Action the action object that is created
     */
	public static Action createAction(String actionName, API api) {
		Map<String, String> actionData = findActionData(actionName, api.getName());
		
		if (actionData == null) {
			var errorMessage = SentinelStringUtils.format("Data for the action {} could not be found in the {}.yml file.", actionName, api.getName());
			throw new NoSuchActionException(errorMessage);
		}
		
		Map.Entry<String, String> firstEntry = actionData.entrySet().iterator().next();
		switch (firstEntry.getKey()) {
		case "GET":
			return new GET(firstEntry.getValue());
		case "POST":
			return new POST(firstEntry.getValue());
		case "PUT":
			return new PUT(firstEntry.getValue());
		default:
			var errorMessage = SentinelStringUtils.format(
					"Action {} did not contain a GET, PUT, or POST entry in the {}.yml file.", actionName,
					api.getName());
			throw new NoSuchActionException(errorMessage);
		}
	}

    /**
     * Returns a Map &lt;String, String&gt; which contains all data for an action that is declared in the API object YAML file for the given API.
     * @param actionName String the name of the action
     * @param apiName String the name of the API
     * @return Map &lt;String, String&gt; the collection of keys and values which were declared in the API object YAML file for the given action in the given API.
     */
    private static Map<String, String> findActionData(String actionName, String apiName) {
		Map<String, String> actionData = Configuration.getAction(actionName, apiName);
		if (actionData == null) {
			for (String page : Configuration.getPageParts(apiName)) {
				actionData = findActionData(actionName, page);
				if (actionData != null) {
					break;
				}
			}
		}
		return actionData;
	}
}
