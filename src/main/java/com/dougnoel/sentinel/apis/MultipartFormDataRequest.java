package com.dougnoel.sentinel.apis;

import com.dougnoel.sentinel.enums.RequestType;
import com.dougnoel.sentinel.exceptions.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.InputStream;
import java.net.URISyntaxException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MultipartFormDataRequest extends Request {
    protected HttpEntity body;

    /**
     * Creates an InputStream to hold the body.
     * @param inputStream InputStream the InputStream to send in the body.
     */
    public void setBody(String nameOfInput, String boundary, InputStream inputStream, String filename) {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setBoundary(boundary)
                .setMode(HttpMultipartMode.STRICT)
                .setCharset(UTF_8);
        entityBuilder.addBinaryBody(nameOfInput, inputStream, ContentType.MULTIPART_FORM_DATA, filename);
        body = entityBuilder.build();
    }

    /**
     * Construct a request, send it to the active API, and store the response for retrieval.
     * Parameterization is handled at the cucumber step level.
     *
     * @param type com.dougnoel.sentinel.enums.RequestType the type of request to send
     * @param endpoint the endpoint to send the request
     */
    public void createAndSendRequest(RequestType type, String endpoint) {
        endpoint = StringUtils.prependIfMissing(endpoint, "/");
        try {
            switch(type) {
                case DELETE:
                    httpRequest = new HttpDelete(APIManager.getAPI().getURIBuilder( endpoint).build());
                    break;
                case GET:
                    httpRequest = new HttpGet(APIManager.getAPI().getURIBuilder(endpoint).build());
                    break;
                case POST:
                    httpRequest = new HttpPost(APIManager.getAPI().getURIBuilder(endpoint).build());
                    RequestConfig config = RequestConfig.custom().setExpectContinueEnabled(true).build();
                    httpRequest.setConfig(config);
                    ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(body);
                    break;
                case PUT:
                    httpRequest = new HttpPut(APIManager.getAPI().getURIBuilder(endpoint).build());
                    RequestConfig config1 = RequestConfig.custom().setExpectContinueEnabled(true).build();
                    httpRequest.setConfig(config1);
                    ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(body);
                    break;
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
        setHeaders();
        buildURI();
        sendRequest();
    }
}
