package edu.clarkson.gdc.workflow.param;

import edu.clarkson.gdc.workflow.WorkflowContext;

public class ReturnParam implements Param {

	private String key;

	public ReturnParam(String key) {
		super();
		this.key = key;
	}

	@Override
	public Object getValue(Object[] input) {
		return WorkflowContext.get().getReturnContext().get(key);
	}

}
