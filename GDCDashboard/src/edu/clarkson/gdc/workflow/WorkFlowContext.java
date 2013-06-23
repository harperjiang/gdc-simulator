package edu.clarkson.gdc.workflow;

import java.util.HashMap;
import java.util.Map;

public class WorkFlowContext {

	public static WorkFlowContext get() {
		return holder.get();
	}

	static ThreadLocal<WorkFlowContext> holder = new ThreadLocal<WorkFlowContext>() {
		@Override
		protected WorkFlowContext initialValue() {
			return new WorkFlowContext();
		}
	};

	private Map<String, Object> context;

	public WorkFlowContext() {
		super();
		context = new HashMap<String, Object>();
	}

	public Map<String, Object> getContext() {
		return context;
	}
}
