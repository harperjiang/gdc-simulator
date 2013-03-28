package edu.clarkson.gdc.simulator.framework.session;

import java.util.HashMap;
import java.util.Map;

public class Session {

	private String uuid;

	private Map<String, Object> context;

	private long timeToLive;

	public Session() {
		super();
		context = new HashMap<String, Object>();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

}
