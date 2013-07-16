package edu.clarkson.gdc.workflow.param;

import edu.clarkson.gdc.workflow.WorkflowContext;

public class ContextParam implements Param {

	protected String key;

	public ContextParam(String key) {
		this.key = key;
	}

	@Override
	public Object getValue(Object[] input) {
		return WorkflowContext.get().getContext().get(key);
	}

}
