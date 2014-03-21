package gdc.network.ripeatlas.api.measurement;

import gdc.network.ripeatlas.api.Configuration;
import gdc.network.ripeatlas.api.common.Request;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class MeasurementResultRequest extends
		Request<MeasurementResultResponse> {

	public MeasurementResultRequest() {
		super(MessageFormat.format("{0}{1}", Configuration.BASE_URL,
				"api/v1/measurement/{0}/result"));
	}

	@Override
	protected HttpUriRequest buildRequest() {
		String url = MessageFormat.format(getUrl(), getMeasurementId());
		return new HttpGet(url);
	}

	@Override
	protected MeasurementResultResponse buildResponse(HttpResponse response)
			throws IOException {
		return new MeasurementResultResponse(response);
	}

	private String measurementId;

	public String getMeasurementId() {
		return measurementId;
	}

	public void setMeasurementId(String measurementId) {
		this.measurementId = measurementId;
	}
}
