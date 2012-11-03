package edu.clarkson.gdc.simulator.stat;

import java.math.BigDecimal;

public class StatOutput {

	/**
	 * Unit in ms
	 */
	private long latency;

	private BigDecimal downtime;

	private BigDecimal availability;

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

	public BigDecimal getDowntime() {
		return downtime;
	}

	public void setDowntime(BigDecimal downtime) {
		this.downtime = downtime;
	}

	public BigDecimal getAvailability() {
		return availability;
	}

	public void setAvailability(BigDecimal availability) {
		this.availability = availability;
	}

}
