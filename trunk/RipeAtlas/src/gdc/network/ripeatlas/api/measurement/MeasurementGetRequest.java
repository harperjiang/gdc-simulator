package gdc.network.ripeatlas.api.measurement;

import gdc.network.ripeatlas.api.Configuration;
import gdc.network.ripeatlas.api.common.Request;

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

		HttpGet get = new HttpGet(MessageFormat.format("{0}{1}", getUrl(),
				measurementId));
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
