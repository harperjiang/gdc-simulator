package edu.clarkson.cs.gdc.network.ripeatlas.api.measurement;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;

import edu.clarkson.cs.gdc.network.common.QueryUtils;
import edu.clarkson.cs.gdc.network.common.Request;
import edu.clarkson.cs.gdc.network.ripeatlas.api.Configuration;

public class MeasurementStopRequest extends
		Request<MeasurementStopResponse> {

	public MeasurementStopRequest() {
		super(MessageFormat.format("{0}{1}", Configuration.BASE_URL,
				"api/v1/measurement/"));
	}

	@Override
	protected HttpUriRequest buildRequest() {
		HttpDelete delete = new HttpDelete(MessageFormat.format("{0}?{1}", getUrl(),
				QueryUtils.queryString("key", Configuration.KEY_STOP_M)));

		return delete;
	}

	@Override
	protected MeasurementStopResponse buildResponse(HttpResponse response)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
