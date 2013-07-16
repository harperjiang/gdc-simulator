package edu.clarkson.gdc.workflow.param;

public abstract class OperandParam implements Param {

	private Param first;

	private Param second;

	public OperandParam(Param first, Param second) {
		this.first = first;
		this.second = second;
	}

	public Param getFirst() {
		return first;
	}

	public void setFirst(Param first) {
		this.first = first;
	}

	public Param getSecond() {
		return second;
	}

	public void setSecond(Param second) {
		this.second = second;
	}

}
