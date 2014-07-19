package edu.clarkson.cs.network.ripeatlas.api.measurement;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

import edu.clarkson.cs.network.ripeatlas.api.common.RipeAtlasResponse;

public class MeasurementStopResponse extends RipeAtlasResponse<Object> {

	protected MeasurementStopResponse(HttpResponse response)
			throws IOException {
		super(response);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object buildResult(JsonElement json) {
		// TODO Auto-generated method stub
		return null;
	}

}
