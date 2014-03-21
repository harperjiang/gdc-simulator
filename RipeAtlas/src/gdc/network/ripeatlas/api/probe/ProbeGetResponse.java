package gdc.network.ripeatlas.api.probe;

import gdc.network.ripeatlas.api.common.Environment;
import gdc.network.ripeatlas.api.common.Response;
import gdc.network.ripeatlas.model.Probe;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

public class ProbeGetResponse extends Response<Probe> {

	protected ProbeGetResponse(HttpResponse response) throws IOException {
		super(response);
	}

	@Override
	protected Probe buildResult(JsonElement json) {
		return Environment.getEnvironment().getParser()
				.fromJson(json, Probe.class);
	}

}
