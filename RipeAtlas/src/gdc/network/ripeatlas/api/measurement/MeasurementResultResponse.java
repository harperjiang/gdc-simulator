package gdc.network.ripeatlas.api.measurement;

import gdc.network.ripeatlas.api.common.Environment;
import gdc.network.ripeatlas.api.common.Response;
import gdc.network.ripeatlas.model.MeasurementResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

public class MeasurementResultResponse extends
		Response<List<MeasurementResult>> {

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
