package edu.clarkson.gdc.simulator.scenario;

import java.math.BigDecimal;

public class Averager {

	private BigDecimal sum;
	private long count;

	public Averager() {
		reset();
	}

	public void reset() {
		count = 0;
		sum = BigDecimal.ZERO;
	}

	public void add(long val) {
		add(new BigDecimal(val));
	}

	public void add(BigDecimal val) {
		count++;
		sum = sum.add(val);
	}

	public BigDecimal getAverage() {
		if (0 == count)
			return BigDecimal.ZERO;
		return sum.divide(new BigDecimal(count), 4, BigDecimal.ROUND_HALF_UP);
	}

	public long getCount() {
		return count;
	}

}
