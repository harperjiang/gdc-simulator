package edu.clarkson.gdc.workflow.param;

public class OrParam extends OperandParam {

	public OrParam(Param first, Param second) {
		super(first, second);
	}

	@Override
	public Object getValue(Object[] input) {
		Object firstResult = getFirst().getValue(input);
		return (firstResult == null) ? getSecond().getValue(input)
				: firstResult;
	}

}
