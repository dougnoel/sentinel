package sentinel.utils;

import cucumber.api.Scenario;

public class Feature {
	
	private Feature(Scenario scenario) {
		//Exists to defeat instantiation
	}

	public static String getFeatureName(Scenario scenario) {
	    String featureName = "Feature ";
	    String rawFeatureName = scenario.getId().split(";")[0].replace("-"," ");
	    featureName = featureName + rawFeatureName.substring(0, 1).toUpperCase() + rawFeatureName.substring(1);

	    return featureName;
	}
}
