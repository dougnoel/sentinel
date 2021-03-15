package com.dougnoel.sentinel.strings;

import io.cucumber.java.Scenario;

public class Feature {
	
	private Feature() {
		//Exists to defeat instantiation
	}

	public static String getFeatureName(Scenario scenario) {
	    String featureName = "Feature ";
	    String rawFeatureName = scenario.getId().split(";")[0].replace("-"," ");
	    featureName = featureName + rawFeatureName.substring(0, 1).toUpperCase() + rawFeatureName.substring(1);

	    return featureName;
	}
}
