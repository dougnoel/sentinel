package com.dougnoel.sentinel.apis;

import java.io.IOException;

import org.apache.http.HttpResponse;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

public class Response {
	
	protected HttpResponse httpResponse;
	protected String jsonResponse;
	
	public Response(HttpResponse httpResponse) throws IOException {
		this.httpResponse = httpResponse;
		this.jsonResponse = SentinelStringUtils.inputStreamToString(this.httpResponse.getEntity().getContent()); //This has to be done when we first get the response because once we read the stream, it is gone.
	}
	
	public void addJsonResponse(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public String getResponse() {
		return jsonResponse;
	}
	
	public Integer getResponseCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}
	
}
