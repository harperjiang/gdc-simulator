package edu.clarkson.cs.network.ripeatlas.api.measurement;

import edu.clarkson.cs.network.common.Request;
import edu.clarkson.cs.network.ripeatlas.api.Configuration;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class MeasurementGetRequest extends Request<MeasurementGetResponse> {

	public MeasurementGetRequest() {
		super(MessageFormat.format("{0}{1}", Configuration.BASE_URL,
				"api/v1/measurement/"));
	}

	@Override
	protected HttpUriRequest buildRequest() {
		Validate.isTrue(!StringUtils.isEmpty(measurementId),
				"Measurement Id cannot be empty");
		String url = MessageFormat.format("{0}{1}/?key={2}", getUrl(),
				measurementId, Configuration.KEY_GET_M);
		if (logger.isDebugEnabled()) {
			logger.debug("Getting measurement:" + url);
		}
		HttpGet get = new HttpGet(url);
		return get;
	}

	@Override
	protected MeasurementGetResponse buildResponse(HttpResponse response)
			throws IOException {
		return new MeasurementGetResponse(response);
	}

	private String measurementId;

	public String getMeasurementId() {
		return measurementId;
	}

	public void setMeasurementId(String measurementId) {
		this.measurementId = measurementId;
	}

}
