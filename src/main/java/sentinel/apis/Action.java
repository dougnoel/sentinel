package sentinel.apis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.client.ClientProtocolException;

/**
 * An abstract class that defines an action for an API object.
 * 
 * Implemented by GET, PUT, and POST concrete classes. For sending a request and receiving a response. Actual
 * requests and responses are stored in Request and Response object collections.
 * 
 * @author Doug Noël
 *
 */
public abstract class Action {
	protected String endpoint = null;
	
	public Action(String endpoint) {
		this.endpoint = endpoint;
	}
		
	/**
	 * Returns a URL as a String with the endpoint attached. 
	 * @param url java.net.URL URL of the API
	 * @return String the URL with the Action's endpoint attached.
	 */
	public String getURI(URL url) {
		String uri = url.toString();
		return (uri.endsWith("/") ? uri : uri + "/") + endpoint;
	}
	
	/**
	 * Returns a response from a given api using a given request.
	 * @param request Request the Sentinel Request object to create and send the request
	 * @param api API the Sentinel API object to make a call against, which contains the URL to use
	 * @return Response the Sentinel Response object returned for evaluation
	 * @throws ClientProtocolException if the HttpClient execution fails
	 * @throws IOException if the HttpClient execution fails or reading the response IOStream fails
	 * @throws URISyntaxException if building the URI fails
	 */
	public abstract Response sendRequest(Request request, API api) throws ClientProtocolException, IOException, URISyntaxException;
}
