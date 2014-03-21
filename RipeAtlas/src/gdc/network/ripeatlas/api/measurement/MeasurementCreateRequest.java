package gdc.network.ripeatlas.api.measurement;

import gdc.network.ripeatlas.api.Configuration;
import gdc.network.ripeatlas.api.common.Environment;
import gdc.network.ripeatlas.api.common.QueryUtils;
import gdc.network.ripeatlas.api.common.Request;
import gdc.network.ripeatlas.model.MeasurementCreate;
import gdc.network.ripeatlas.model.ProbeSpec;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MeasurementCreateRequest extends
		Request<MeasurementCreateResponse> {

	public MeasurementCreateRequest() {
		super(MessageFormat.format("{0}{1}", Configuration.BASE_URL,
				"api/v1/measurement/"));
		probes = new ArrayList<ProbeSpec>();
	}

	@Override
	protected HttpUriRequest buildRequest() {
		HttpPost post = new HttpPost(MessageFormat.format("{0}?{1}", getUrl(),
				QueryUtils.queryString("key", Configuration.KEY_CREATE_M)));
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept", "application/json");

		JsonElement measurement = Environment.getEnvironment().getParser()
				.toJsonTree(getMeasurement());
		JsonObject entity = new JsonObject();
		JsonArray array = new JsonArray();
		array.add(measurement);
		entity.add("definitions", array);
		entity.add("probes", Environment.getEnvironment().getParser()
				.toJsonTree(probes));

		String json = Environment.getEnvironment().getParser().toJson(entity);
		if (logger.isDebugEnabled()) {
			logger.debug("Create Measurement:" + json);
		}
		try {
			post.setEntity(new ByteArrayEntity(json.getBytes("utf8")));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return post;
	}

	@Override
	protected MeasurementCreateResponse buildResponse(HttpResponse response)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	private MeasurementCreate measurement;

	private List<ProbeSpec> probes;

	public MeasurementCreate getMeasurement() {
		return measurement;
	}

	public void setMeasurement(MeasurementCreate measurement) {
		this.measurement = measurement;
	}

	public List<ProbeSpec> getProbes() {
		return probes;
	}

}
