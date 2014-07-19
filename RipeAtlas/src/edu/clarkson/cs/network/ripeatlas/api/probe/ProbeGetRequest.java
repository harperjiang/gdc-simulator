package edu.clarkson.cs.network.ripeatlas.api.probe;

import edu.clarkson.cs.network.common.Request;
import edu.clarkson.cs.network.ripeatlas.api.Configuration;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class ProbeGetRequest extends Request<ProbeGetResponse> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProbeGetRequest() {
		super(MessageFormat.format("{0}{1}", Configuration.BASE_URL,
				"api/v1/probe/"));
	}

	@Override
	protected HttpUriRequest buildRequest() {
		HttpGet get = new HttpGet(MessageFormat.format("{0}{1}/", getUrl(), id));
		return get;
	}

	@Override
	protected ProbeGetResponse buildResponse(HttpResponse response)
			throws IOException {
		return new ProbeGetResponse(response);
	}

}
