package edu.clarkson.gdc.dashboard.domain.entity;

import java.math.BigDecimal;

public class Summary {

	private int dcCount;

	private int dcRunning;

	private BigDecimal utilization;

	private int mtbm;

	private int usage;

	private int capacity;

	public int getDcCount() {
		return dcCount;
	}

	public void setDcCount(int dcCount) {
		this.dcCount = dcCount;
	}

	public int getDcRunning() {
		return dcRunning;
	}

	public void setDcRunning(int dcRunning) {
		this.dcRunning = dcRunning;
	}

	public BigDecimal getUtilization() {
		return utilization;
	}

	public void setUtilization(BigDecimal utilization) {
		this.utilization = utilization;
	}

	public int getMtbm() {
		return mtbm;
	}

	public void setMtbm(int mtbm) {
		this.mtbm = mtbm;
	}

	public int getUsage() {
		return usage;
	}

	public void setUsage(int usage) {
		this.usage = usage;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
