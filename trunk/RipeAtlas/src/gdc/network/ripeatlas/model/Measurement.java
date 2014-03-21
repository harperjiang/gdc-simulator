package gdc.network.ripeatlas.model;

import gdc.network.ripeatlas.api.common.deserializer.JsonAttribute;

import java.util.Date;

public class Measurement {

	@JsonAttribute("msm_id")
	private int id;

	private String type;

	private Date creationTime;

	private String description;

	private Date startTime;

	private Date stopTime;

	private boolean isOneoff;

	private boolean isPublic;

	private int interval;

	private String dstAddr;

	private int dstAsn;

	private String dstName;

	private Probe[] probeSources;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public boolean isOneoff() {
		return isOneoff;
	}

	public void setOneoff(boolean isOneoff) {
		this.isOneoff = isOneoff;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getDstAddr() {
		return dstAddr;
	}

	public void setDstAddr(String dstAddr) {
		this.dstAddr = dstAddr;
	}

	public int getDstAsn() {
		return dstAsn;
	}

	public void setDstAsn(int dstAsn) {
		this.dstAsn = dstAsn;
	}

	public String getDstName() {
		return dstName;
	}

	public void setDstName(String dstName) {
		this.dstName = dstName;
	}

	public Probe[] getProbeSources() {
		return probeSources;
	}

	public void setProbeSources(Probe[] probeSources) {
		this.probeSources = probeSources;
	}

}
