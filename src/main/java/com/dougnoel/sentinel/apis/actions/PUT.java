package com.dougnoel.sentinel.apis.actions;

import java.io.IOException;

import com.dougnoel.sentinel.apis.API;
import com.dougnoel.sentinel.apis.Request;
import com.dougnoel.sentinel.apis.Response;

public class PUT extends Action {

	public PUT(String endpoint) {
		super(endpoint);
	}
	
	@Override
	public Response sendRequest(Request request, API api) throws IOException {
		return null;
	}

}
