package gdc.network.ripeatlas.api.common;

import gdc.network.ripeatlas.api.common.deserializer.BeanDeserializer;
import gdc.network.ripeatlas.api.common.deserializer.MeasurementDeserializer;
import gdc.network.ripeatlas.api.common.deserializer.MeasurementResultDeserializer;
import gdc.network.ripeatlas.api.common.deserializer.TracerouteDeserializer;
import gdc.network.ripeatlas.api.common.serializer.BeanSerializer;
import gdc.network.ripeatlas.model.Measurement;
import gdc.network.ripeatlas.model.MeasurementCreate;
import gdc.network.ripeatlas.model.MeasurementResult;
import gdc.network.ripeatlas.model.Probe;
import gdc.network.ripeatlas.model.ProbeSpec;
import gdc.network.ripeatlas.model.TracerouteOutput;
import gdc.network.ripeatlas.model.TracerouteTarget;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class Environment {

	private Environment() {
		super();
		httpClient = HttpClients.createDefault();

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(MeasurementResult.class,
				new MeasurementResultDeserializer());
		builder.registerTypeAdapter(TracerouteOutput.class,
				new TracerouteDeserializer());

		builder.registerTypeAdapter(Measurement.class,
				new MeasurementDeserializer());
		builder.registerTypeAdapter(Probe.class, new BeanDeserializer<Probe>());

		builder.registerTypeAdapter(MeasurementCreate.class,
				new BeanSerializer<MeasurementCreate>());
		builder.registerTypeAdapter(TracerouteTarget.class,
				new BeanSerializer<TracerouteTarget>());
		builder.registerTypeAdapter(ProbeSpec.class,
				new BeanSerializer<ProbeSpec>());

		parser = builder.create();
		reader = new JsonParser();
	}

	private static Environment instance;
	static {
		instance = new Environment();
	}

	public static Environment getEnvironment() {
		return instance;
	}

	private HttpClient httpClient;

	private Gson parser;

	private JsonParser reader;

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public Gson getParser() {
		return parser;
	}

	public JsonParser getReader() {
		return reader;
	}
}
