package edu.clarkson.gdc.simulator.framework.session;

import java.util.HashMap;
import java.util.Map;

public class Session {

	private String id;

	private Map<String, Object> context;

	private long timeToLive;

	public Session() {
		super();
		context = new HashMap<String, Object>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void put(String key, Object value) {
		context.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) context.get(key);
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

}
