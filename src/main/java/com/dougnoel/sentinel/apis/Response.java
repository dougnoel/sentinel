package com.dougnoel.sentinel.apis;

import java.io.IOException;
import java.time.Duration;

import org.apache.http.HttpResponse;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

/**
 * Wrapper for an http response for testing the response.
 * @author dougnoel
 *
 */
public class Response {
	
	protected HttpResponse httpResponse;
	protected String jsonResponse;
	protected Duration responseTime;
	
	/**
	 * 
	 * @param httpResponse HttpResponse the API call response used to create this object
	 * @throws IOException if the parsing fails
	 */
	public Response(HttpResponse httpResponse) throws IOException {
		this.httpResponse = httpResponse;
		this.jsonResponse = SentinelStringUtils.inputStreamToString(this.httpResponse.getEntity().getContent()); //This has to be done when we first get the response because once we read the stream, it is gone.
	}

	/**
	 * Returns the http response as a String
	 * @deprecated
	 * This method is no longer needed for comparison operations.
	 * @return String the http response
	 */
	@Deprecated
	public String getResponse() {
		return jsonResponse;
	}
	
	/**
	 * Returns the response code from the response
	 * @return int the status of the response
	 */
	public int getResponseCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}
	
	/**
	 * Sets the amount of time that the response took to get.
	 * @param duration
	 */
	public void setResponseTime(Duration duration) {
		responseTime = duration;
	}
	
	/**
	 * Returns the amount of time the response took to get.
	 * @return Duration the response time
	 */
	public Duration getReponseTime() {
		return responseTime;
	}

	/**
	 * Compares only the actual contents of the response when creating a hashcode.
	 * This was code automatically generated using Elcipse.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jsonResponse == null) ? 0 : jsonResponse.hashCode());
		return result;
	}

	/**
	 * Compares only the actual contents of the response. Is also used by contains().
	 * This was code automatically generated using Elcipse.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Response other = (Response) obj;
		if (jsonResponse == null) {
			if (other.jsonResponse != null)
				return false;
		} 
		else if (!jsonResponse.equals(other.jsonResponse))
			return false;
		return true;
	}

	/**
	 * @param s CharSequence 
	 * @return boolean 
	 * @see java.lang.String#contains(java.lang.CharSequence)
	 */
	public boolean contains(CharSequence s) {
		return jsonResponse.contains(s);
	}
	
}
