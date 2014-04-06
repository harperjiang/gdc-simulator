package gdc.network.ripeatlas.model;

import gdc.network.ripeatlas.api.common.deserializer.JsonAttribute;

import java.util.Date;

public class Measurement {
	
	public static interface Status {
		int Specified = 0;
		int Scheduled = 1;
		int Ongoing = 2;
		int Stopped = 4;
		int Forced_to_stop = 5;
		int No_suitable_probes = 6;
		int Failed = 7;
		int Archived = 8;
	}

	@JsonAttribute("msm_id")
	private int id;

	private int af;

	private String type;

	private Date creationTime;

	private String description;

	private Date startTime;

	private Date stopTime;

	@JsonAttribute("is_oneoff")
	private boolean oneoff;

	@JsonAttribute("is_public")
	private boolean publicc;

	private int interval;

	private String dstAddr;

	private int dstAsn;

	private String dstName;

	private int status;

	private Probe[] probeSources;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Probe[] getProbeSources() {
		return probeSources;
	}

	public void setProbeSources(Probe[] probeSources) {
		this.probeSources = probeSources;
	}

}
