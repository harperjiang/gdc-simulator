package edu.clarkson.cs.gdc.network.common;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import edu.clarkson.cs.gdc.network.common.deserializer.BeanDeserializer;
import edu.clarkson.cs.gdc.network.common.deserializer.MeasurementDeserializer;
import edu.clarkson.cs.gdc.network.common.deserializer.MeasurementResultDeserializer;
import edu.clarkson.cs.gdc.network.common.deserializer.TracerouteDeserializer;
import edu.clarkson.cs.gdc.network.common.serializer.BeanSerializer;
import edu.clarkson.cs.gdc.network.ipquery.model.IPInfo;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Measurement;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Probe;
import edu.clarkson.cs.gdc.network.ripeatlas.model.ProbeSpec;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteOutput;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteTarget;

public class Environment {

	private Environment() {
		super();
		httpClient = HttpClients.createDefault();

		GsonBuilder builder = new GsonBuilder();

		// Serializer/Deserializers for RipeAtlas
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

		// Serializers/Deserializers for IPAddress
		builder.registerTypeAdapter(IPInfo.class, new BeanDeserializer<IPInfo>());
		
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
