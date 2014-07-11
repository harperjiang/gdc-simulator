package edu.clarkson.cs.gdc.network.ripeatlas.api.probe;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

import edu.clarkson.cs.gdc.network.common.Environment;
import edu.clarkson.cs.gdc.network.ripeatlas.api.common.RipeAtlasResponse;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Probe;

public class ProbeGetResponse extends RipeAtlasResponse<Probe> {

	protected ProbeGetResponse(HttpResponse response) throws IOException {
		super(response);
	}

	@Override
	protected Probe buildResult(JsonElement json) {
		return Environment.getEnvironment().getParser()
				.fromJson(json, Probe.class);
	}

}
