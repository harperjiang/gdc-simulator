package edu.clarkson.cs.network.ripeatlas.model;

public class TracerouteTarget extends MeasurementTarget {

	public TracerouteTarget() {
		super();
		setType("traceroute");
	}

	private String target;

	private String protocol;

	private int interval = 900;

	private int paris = 1;

	private int firsthop = 1;

	private int maxhops = 64;

	private int timeout = 100;

	private int size = 40;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getParis() {
		return paris;
	}

	public void setParis(int paris) {
		this.paris = paris;
	}

	public int getFirsthop() {
		return firsthop;
	}

	public void setFirsthop(int firsthop) {
		this.firsthop = firsthop;
	}

	public int getMaxhops() {
		return maxhops;
	}

	public void setMaxhops(int maxhops) {
		this.maxhops = maxhops;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
