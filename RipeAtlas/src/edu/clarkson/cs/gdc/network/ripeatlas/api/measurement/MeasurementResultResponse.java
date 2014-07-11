package edu.clarkson.cs.gdc.network.ripeatlas.api.measurement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

import edu.clarkson.cs.gdc.network.common.Environment;
import edu.clarkson.cs.gdc.network.ripeatlas.api.common.RipeAtlasResponse;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementResult;

public class MeasurementResultResponse extends
		RipeAtlasResponse<List<MeasurementResult>> {

	protected MeasurementResultResponse(HttpResponse response)
			throws IOException {
		super(response);
	}

	@Override
	protected List<MeasurementResult> buildResult(JsonElement json) {
		List<MeasurementResult> results = new ArrayList<MeasurementResult>();

		for (JsonElement element : json.getAsJsonArray()) {
			results.add(Environment.getEnvironment().getParser()
					.fromJson(element, MeasurementResult.class));
		}

		return results;
	}

}
