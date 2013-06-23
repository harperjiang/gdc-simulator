package edu.clarkson.gdc.workflow;

import java.util.HashMap;
import java.util.Map;

public class WorkflowContext {

	public static WorkflowContext get() {
		return holder.get();
	}

	static ThreadLocal<WorkflowContext> holder = new ThreadLocal<WorkflowContext>() {
		@Override
		protected WorkflowContext initialValue() {
			return new WorkflowContext();
		}
	};

	private Map<String, Object> context;

	public WorkflowContext() {
		super();
		context = new HashMap<String, Object>();
	}

	public Map<String, Object> getContext() {
		return context;
	}
}
