package edu.clarkson.cs.network.ripeatlas.api.measurement;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

import edu.clarkson.cs.network.ripeatlas.api.common.RipeAtlasResponse;

public class MeasurementCreateResponse extends RipeAtlasResponse<Integer> {

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
