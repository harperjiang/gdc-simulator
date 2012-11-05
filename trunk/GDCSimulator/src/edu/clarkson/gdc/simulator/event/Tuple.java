package edu.clarkson.gdc.simulator.event;

public class Tuple {

	public Tuple(Object from, Object to) {
		super();
		this.from = from;
		this.to = to;
	}

	private Object from;

	private Object to;

	public Object getFrom() {
		return from;
	}

	public void setFrom(Object from) {
		this.from = from;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}

}
