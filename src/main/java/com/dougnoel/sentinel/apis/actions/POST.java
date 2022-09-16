package com.dougnoel.sentinel.apis.actions;

import java.io.IOException;

import com.dougnoel.sentinel.apis.API;
import com.dougnoel.sentinel.apis.Request;
import com.dougnoel.sentinel.apis.Response;

public class POST extends Action {

	public POST(String endpoint) {
		super(endpoint);
	}
	
	@Override
	public Response sendRequest(Request request, API api) throws IOException {
		return null;
	}
	
	/**
	 * Ensures that headers, URL and request are not null;
	 * @return response from the URL
	 */

}
