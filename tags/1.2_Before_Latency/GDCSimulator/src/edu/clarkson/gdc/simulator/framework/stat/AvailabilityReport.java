package edu.clarkson.gdc.simulator.framework.stat;

import java.math.BigDecimal;
import java.util.List;

public class AvailabilityReport {

	public static final class PeriodStatus {
		long start;
		long stop;
		boolean status;

		public long getStart() {
			return start;
		}

		public void setStart(long start) {
			this.start = start;
		}

		public long getStop() {
			return stop;
		}

		public void setStop(long stop) {
			this.stop = stop;
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

	}

	/**
	 * Total Time in Milliseconds
	 */
	private long totalTime;

	private long requestCount;

	private long failedCount;

	private List<PeriodStatus> status;

	private long readKeyCount;

	private long readKeyTime;

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public long getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
	}

	public long getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(long failedCount) {
		this.failedCount = failedCount;
	}

	public BigDecimal getAvailability() {
		if (getRequestCount() == 0)
			return null;
		return new BigDecimal(getFailedCount()).divide(new BigDecimal(
				getRequestCount()), 4, BigDecimal.ROUND_HALF_UP);
	}

	public List<PeriodStatus> getStatus() {
		return status;
	}

	public void setStatus(List<PeriodStatus> status) {
		this.status = status;
	}

	public long getReadKeyCount() {
		return readKeyCount;
	}

	public void setReadKeyCount(long readKeyCount) {
		this.readKeyCount = readKeyCount;
	}

	public long getReadKeyTime() {
		return readKeyTime;
	}

	public void setReadKeyTime(long readKeyTime) {
		this.readKeyTime = readKeyTime;
	}

	public BigDecimal getAverageResponseTime() {
		return new BigDecimal(readKeyTime).divide(new BigDecimal(readKeyCount),
				2, BigDecimal.ROUND_HALF_UP);
	}
}
