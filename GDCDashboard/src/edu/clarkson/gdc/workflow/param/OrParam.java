package edu.clarkson.gdc.workflow.param;

public class OrParam implements Param {

	private Param first;

	private Param second;

	public OrParam(Param first, Param second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public Object getValue(Object[] input) {
		Object firstResult = this.first.getValue(input);
		return (firstResult == null) ? second.getValue(input) : firstResult;
	}

}
