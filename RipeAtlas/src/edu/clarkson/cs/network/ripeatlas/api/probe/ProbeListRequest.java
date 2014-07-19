package edu.clarkson.cs.network.ripeatlas.api.probe;

import edu.clarkson.cs.network.common.ListRequest;
import edu.clarkson.cs.network.ripeatlas.api.Configuration;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;

public class ProbeListRequest extends ListRequest<ProbeListResponse> {

	public ProbeListRequest() {
		super(MessageFormat.format("{0}{1}", Configuration.BASE_URL,
				"api/v1/probe/"));
	}

	@Override
	protected ProbeListResponse buildResponse(HttpResponse response)
			throws IOException {
		return new ProbeListResponse(response);
	}

}
