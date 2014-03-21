package gdc.network.ripeatlas.model;

public class ProbeSpec {

	public static enum Type {
		area, country, prefix, asn, probes, msm
	}

	public static final String WW = "WW";

	public static final String West = "West";

	public static final String North_Central = "North-Central";

	public static final String South_Central = "South-Central";

	public static final String North_East = "North-East";

	public static final String South_East = "South-East";

	private int requested;

	private String type;

	private String value;

	public int getRequested() {
		return requested;
	}

	public void setRequested(int requested) {
		this.requested = requested;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
