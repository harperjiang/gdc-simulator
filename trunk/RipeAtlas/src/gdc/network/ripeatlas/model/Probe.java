package gdc.network.ripeatlas.model;

import gdc.network.ripeatlas.api.common.deserializer.JsonAttribute;

import java.util.Date;

public class Probe {

	private int id;

	private String addressV4;

	private String addressV6;

	private int asnV4;

	private int asnV6;

	private String countryCode;

	@JsonAttribute("is_anchor")
	private boolean anchor;

	@JsonAttribute("is_public")
	private boolean publicc;

	private String resourceUri;

	private String prefixV4;

	private String prefixV6;

	private int status;

	private Date statusSince;

	private Double latitude;

	private Double longitude;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddressV4() {
		return addressV4;
	}

	public void setAddressV4(String addressV4) {
		this.addressV4 = addressV4;
	}

	public String getAddressV6() {
		return addressV6;
	}

	public void setAddressV6(String addressV6) {
		this.addressV6 = addressV6;
	}

	public int getAsnV4() {
		return asnV4;
	}

	public void setAsnV4(int asnV4) {
		this.asnV4 = asnV4;
	}

	public int getAsnV6() {
		return asnV6;
	}

	public void setAsnV6(int asnV6) {
		this.asnV6 = asnV6;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isAnchor() {
		return anchor;
	}

	public void setAnchor(boolean anchor) {
		this.anchor = anchor;
	}

	public boolean isPublicc() {
		return publicc;
	}

	public void setPublicc(boolean publicc) {
		this.publicc = publicc;
	}

	public String getResourceUri() {
		return resourceUri;
	}

	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

	public String getPrefixV4() {
		return prefixV4;
	}

	public void setPrefixV4(String prefixV4) {
		this.prefixV4 = prefixV4;
	}

	public String getPrefixV6() {
		return prefixV6;
	}

	public void setPrefixV6(String prefixV6) {
		this.prefixV6 = prefixV6;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStatusSince() {
		return statusSince;
	}

	public void setStatusSince(Date statusSince) {
		this.statusSince = statusSince;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getStatusText() {
		if (getStatus() < 0 || getStatus() >= ProbeStatus.values().length)
			return null;
		return ProbeStatus.values()[getStatus()].name();
	}
}
