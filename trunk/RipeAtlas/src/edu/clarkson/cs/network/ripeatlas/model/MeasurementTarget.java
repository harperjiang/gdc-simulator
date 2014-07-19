package edu.clarkson.cs.network.ripeatlas.model;

import edu.clarkson.cs.network.common.deserializer.JsonAttribute;

public class MeasurementTarget {

	private String description;

	private int af = 4;

	private String type;

	private boolean resolveOnProbe;

	@JsonAttribute("is_oneoff")
	private boolean oneoff;

	@JsonAttribute("is_public")
	private boolean publicc;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAf() {
		return af;
	}

	public void setAf(int af) {
		this.af = af;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isResolveOnProbe() {
		return resolveOnProbe;
	}

	public void setResolveOnProbe(boolean resolveOnProbe) {
		this.resolveOnProbe = resolveOnProbe;
	}

	public boolean isOneoff() {
		return oneoff;
	}

	public void setOneoff(boolean oneoff) {
		this.oneoff = oneoff;
	}

	public boolean isPublicc() {
		return publicc;
	}

	public void setPublicc(boolean publicc) {
		this.publicc = publicc;
	}

}
