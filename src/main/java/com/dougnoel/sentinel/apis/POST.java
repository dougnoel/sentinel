package com.dougnoel.sentinel.apis;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;

public class POST extends Action {

	public POST(String endpoint) {
		super(endpoint);
	}
	
	@Override
	public Response sendRequest(Request request, API api) throws ClientProtocolException, IOException {
		return null;
	}
	
	/**
	 * Ensures that headers, URL and request are not null;
	 * @return response from the URL
	 */
//	public String sendRequest(URL url) {
//		String response = null;
////		if (headers.isEmpty()) { //|| requests.isEmpty() || url == null) {
////			response = "Missing information. Cannot send request.";
////		} else {
//			//Send request
//			//process response
//			response = responseCode + jsonResponse;
////		}
//		return response;
//	}
	
//	public void doPostAndPrintResultToConsole(String APIURL, String data) throws ClientProtocolException, IOException {
////		String payload = "{" +
////            "\"title\": \"testStuff\"" +
////            "}";
//	    StringEntity entity = new StringEntity(data, ContentType.APPLICATION_JSON);
//	    CloseableHttpClient httpClient = HttpClients.createDefault();
//		HttpPost httpGet = new HttpPost(APIURL);
//		
//	    if(jwt == null) {
//			setJWT();
//		}
//	    
//	    HttpUriRequest request = RequestBuilder.post()
//				  .setUri(APIURL)
//				  .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
//				  .setHeader(HttpHeaders.AUTHORIZATION, StringUtils.format("Bearer {}", jwt))
//				  .setEntity(entity)
//				  .build();
//	    System.out.println(request.toString());
//	    CloseableHttpResponse response = httpClient.execute(request);
//	    System.out.println(response.getStatusLine().getStatusCode());
//	}

}
