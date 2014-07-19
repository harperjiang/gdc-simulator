package edu.clarkson.cs.network.common;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import edu.clarkson.cs.network.common.deserializer.BeanDeserializer;
import edu.clarkson.cs.network.common.deserializer.MeasurementDeserializer;
import edu.clarkson.cs.network.common.deserializer.MeasurementResultDeserializer;
import edu.clarkson.cs.network.common.deserializer.TracerouteDeserializer;
import edu.clarkson.cs.network.common.serializer.BeanSerializer;
import edu.clarkson.cs.network.ipinfo.model.IPInfo;
import edu.clarkson.cs.network.ripeatlas.model.Measurement;
import edu.clarkson.cs.network.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.network.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.network.ripeatlas.model.Probe;
import edu.clarkson.cs.network.ripeatlas.model.ProbeSpec;
import edu.clarkson.cs.network.ripeatlas.model.TracerouteOutput;
import edu.clarkson.cs.network.ripeatlas.model.TracerouteTarget;

public class Environment {

	private Environment() {
		super();
		// Create HTTP Client
		httpClient = HttpClients.createDefault();

		// Create JSON Parser
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
		builder.registerTypeAdapter(IPInfo.class,
				new BeanDeserializer<IPInfo>());

		parser = builder.create();
		reader = new JsonParser();

		// Prepare JPA environment
		em = Persistence.createEntityManagerFactory("network")
				.createEntityManager();
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

	private EntityManager em;

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public Gson getParser() {
		return parser;
	}

	public JsonParser getReader() {
		return reader;
	}

	public EntityManager getEntityManager() {
		return em;
	}
}
