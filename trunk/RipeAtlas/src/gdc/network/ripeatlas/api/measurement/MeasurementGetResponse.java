package gdc.network.ripeatlas.api.measurement;

import gdc.network.ripeatlas.api.common.Environment;
import gdc.network.ripeatlas.api.common.Response;
import gdc.network.ripeatlas.model.Measurement;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

public class MeasurementGetResponse extends Response<Measurement> {

	public MeasurementGetResponse(HttpResponse response) throws IOException {
		super(response);
	}

	@Override
	protected Measurement buildResult(JsonElement json) {
		return Environment.getEnvironment().getParser()
				.fromJson(json, Measurement.class);
	}
}
