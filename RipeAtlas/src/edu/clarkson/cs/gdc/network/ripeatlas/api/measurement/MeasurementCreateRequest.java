package edu.clarkson.cs.gdc.network.ripeatlas.api.measurement;

import edu.clarkson.cs.gdc.network.ripeatlas.api.Configuration;
import edu.clarkson.cs.gdc.network.ripeatlas.api.common.Environment;
import edu.clarkson.cs.gdc.network.ripeatlas.api.common.QueryUtils;
import edu.clarkson.cs.gdc.network.ripeatlas.api.common.Request;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementCreate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

public class MeasurementCreateRequest extends
		Request<MeasurementCreateResponse> {

	public MeasurementCreateRequest() {
		super(MessageFormat.format("{0}{1}", Configuration.BASE_URL,
				"api/v1/measurement/"));
	}

	@Override
	protected HttpUriRequest buildRequest() {
		HttpPost post = new HttpPost(MessageFormat.format("{0}?{1}", getUrl(),
				QueryUtils.queryString("key", Configuration.KEY_CREATE_M)));
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept", "application/json");

		String json = Environment.getEnvironment().getParser()
				.toJson(getMeasurement());
		if (logger.isDebugEnabled()) {
			logger.debug("Create Measurement:" + json);
		}
		try {
			post.setEntity(new ByteArrayEntity(json.getBytes("utf8")));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return post;
	}

	@Override
	protected MeasurementCreateResponse buildResponse(HttpResponse response)
			throws IOException {
		return new MeasurementCreateResponse(response);
	}

	private MeasurementCreate measurement;

	
	public MeasurementCreate getMeasurement() {
		return measurement;
	}

	public void setMeasurement(MeasurementCreate measurement) {
		this.measurement = measurement;
	}


}
