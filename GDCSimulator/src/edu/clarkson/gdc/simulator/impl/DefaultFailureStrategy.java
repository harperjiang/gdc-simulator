package edu.clarkson.gdc.simulator.impl;

import java.util.List;

import edu.clarkson.gdc.simulator.FailureStrategy;
import edu.clarkson.gdc.simulator.framework.Clock;

/**
 * 
 * @author harper
 * 
 */
public class DefaultFailureStrategy implements FailureStrategy {

	public static class Range {
		
		long start;
		
		long stop;
		
		boolean value;

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

		public boolean getValue() {
			return value;
		}

		public void setValue(boolean value) {
			this.value = value;
		}

	}

	private List<Range> ranges;

	private int index;

	private boolean defaultValue;

	public boolean getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<Range> getRanges() {
		return ranges;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public boolean shouldFail() {
		Range current = getRanges().get(getIndex());
		if (current.getStart() <= Clock.getInstance().getCounter()
				&& current.getStop() > Clock.getInstance().getCounter()) {
			return current.getValue();
		} else {
			index++;
			if (index >= ranges.size()) {// No more data available, fail
				return defaultValue;
			}
			return shouldFail();
		}
	}

	public void load() {

	}

}
