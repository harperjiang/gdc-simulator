package edu.clarkson.gdc.simulator.scenario;

public class Averager {

	private long sum;
	private long count;

	public Averager() {
		reset();
	}

	public void reset() {
		count = 0;
		sum = 0;
	}

	public void add(long val) {
		count++;
		sum += val;
	}

	public long getAverage() {
		if (0 == count)
			return 0;
		return sum / count;
	}

	public long getCount() {
		return count;
	}

}
