package edu.clarkson.gdc.simulator.scenario.tact;

import edu.clarkson.gdc.simulator.framework.DataMessage;

public class Operation implements Comparable<Operation> {

	private Timestamp timestamp;

	private Object load;

	public Operation(long counter, int number, DataMessage message) {
		this.timestamp = new Timestamp(counter, number);
		this.load = message;
	}

	@Override
	public int compareTo(Operation o) {
		return getTimestamp().compareTo(o.getTimestamp());
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Object getLoad() {
		return load;
	}

	public void setLoad(Object load) {
		this.load = load;
	}

}
