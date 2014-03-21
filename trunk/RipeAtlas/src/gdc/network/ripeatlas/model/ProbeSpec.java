package gdc.network.ripeatlas.model;

public class ProbeSpec {

	public static enum Type {
		area, country, prefix, asn, probes, msm
	}

	public static interface Area {
		String WW = "WW";

		String West = "West";

		String North_Central = "North-Central";

		String South_Central = "South-Central";

		String North_East = "North-East";

		String South_East = "South-East";
	}

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
