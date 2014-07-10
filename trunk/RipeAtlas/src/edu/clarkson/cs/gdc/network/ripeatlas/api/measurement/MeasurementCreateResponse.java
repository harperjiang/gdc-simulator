package edu.clarkson.cs.gdc.network.ripeatlas.api.measurement;

import java.io.IOException;

import org.apache.http.HttpResponse;

import edu.clarkson.cs.gdc.network.ripeatlas.api.common.Response;

import com.google.gson.JsonElement;

public class MeasurementCreateResponse extends Response<Integer> {

	protected MeasurementCreateResponse(HttpResponse response)
			throws IOException {
		super(response);
	}

	@Override
	protected Integer buildResult(JsonElement json) {
		return json.getAsJsonObject().get("measurements").getAsJsonArray()
				.get(0).getAsInt();
	}

}
