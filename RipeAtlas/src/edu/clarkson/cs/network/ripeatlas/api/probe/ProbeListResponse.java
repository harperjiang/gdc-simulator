package edu.clarkson.cs.network.ripeatlas.api.probe;

import edu.clarkson.cs.network.common.Environment;
import edu.clarkson.cs.network.ripeatlas.api.common.ListResponse;
import edu.clarkson.cs.network.ripeatlas.model.Probe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;

public class ProbeListResponse extends ListResponse<List<Probe>> {

	protected ProbeListResponse(HttpResponse response) throws IOException {
		super(response);
	}

	@Override
	protected List<Probe> buildResult(JsonElement json) {
		List<Probe> probes = new ArrayList<Probe>();

		metainfo(json);
		for (JsonElement elem : json.getAsJsonObject().get("objects")
				.getAsJsonArray()) {
			Probe probe = Environment.getEnvironment().getParser()
					.fromJson(elem, Probe.class);
			probes.add(probe);
		}

		return probes;
	}

}