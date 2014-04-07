package gdc.network.ripeatlas.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeasurementResult {

	private String dstAddr;

	private String dstName;

	private String from;

	private int measurementId;

	private String protocol;

	private int probeId;

	private String type;

	private int size;

	private Date timestamp;

	private Date endTime;

	private int af;

	private int fw;

	private List<Output> outputs;

	public MeasurementResult() {
		super();
		outputs = new ArrayList<Output>();
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

	public int getMeasurementId() {
		return measurementId;
	}

	public void setMeasurementId(int measurementId) {
		this.measurementId = measurementId;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getProbeId() {
		return probeId;
	}

	public void setProbeId(int probeId) {
		this.probeId = probeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getAf() {
		return af;
	}

	public void setAf(int af) {
		this.af = af;
	}

	public int getFw() {
		return fw;
	}

	public void setFw(int fw) {
		this.fw = fw;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
