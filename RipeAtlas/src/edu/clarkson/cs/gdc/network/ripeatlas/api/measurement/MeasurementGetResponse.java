package edu.clarkson.cs.gdc.network.ripeatlas.api.measurement;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

import edu.clarkson.cs.gdc.network.common.Environment;
import edu.clarkson.cs.gdc.network.ripeatlas.api.common.RipeAtlasResponse;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Measurement;

public class MeasurementGetResponse extends RipeAtlasResponse<Measurement> {

	public MeasurementGetResponse(HttpResponse response) throws IOException {
		super(response);
	}

	@Override
	protected Measurement buildResult(JsonElement json) {
		return Environment.getEnvironment().getParser()
				.fromJson(json, Measurement.class);
	}
}
