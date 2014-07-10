package edu.clarkson.cs.gdc.network.ripeatlas.api.common;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Request<T extends Response<?>> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private String url;

	public Request(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public T execute() throws ClientProtocolException, IOException {
		HttpUriRequest request = buildRequest();
		try {
			HttpResponse response = Environment.getEnvironment()
					.getHttpClient().execute(request);
			return buildResponse(response);
		} catch (ClientProtocolException e) {
			logger.error("Exception should be corrected", e);
			return null;
		}
	}

	protected abstract HttpUriRequest buildRequest();

	protected abstract T buildResponse(HttpResponse response)
			throws IOException;
}
