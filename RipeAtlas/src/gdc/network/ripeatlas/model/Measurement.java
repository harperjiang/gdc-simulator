package gdc.network.ripeatlas.model;

import java.util.Date;

public class Measurement {

	private int id;

	private String type;

	private Date startTime;

	private Date stopTime;

	private int interval;

	private String dstAddr;

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
