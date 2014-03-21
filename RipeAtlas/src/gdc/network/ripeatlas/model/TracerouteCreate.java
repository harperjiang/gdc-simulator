package gdc.network.ripeatlas.model;

public class TracerouteCreate extends MeasurementCreate {

	public TracerouteCreate() {
		super();
		setType("traceroute");
	}

	private String target;
	
	private String protocol;
	
	private int interval;
	
	private int maxhops;
	
	private int timeout;

	private int size;

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
