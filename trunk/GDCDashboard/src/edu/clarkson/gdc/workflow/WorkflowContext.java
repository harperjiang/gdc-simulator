package edu.clarkson.gdc.workflow;

import java.util.HashMap;
import java.util.Map;

public class WorkflowContext {

	private static final String RETURN_CONTEXT = "return_context";

	public static WorkflowContext get() {
		return holder.get();
	}

	static ThreadLocal<WorkflowContext> holder = new ThreadLocal<WorkflowContext>() {
		@Override
		protected WorkflowContext initialValue() {
			return new WorkflowContext();
		}
	};

	public <T extends Object> T get(String key, T defaultVal) {
		@SuppressWarnings("unchecked")
		T t = (T) getContext().get(key);
		if (null == t) {
			getContext().put(key, defaultVal);
			return defaultVal;
		}
		return t;
	}

	private Map<String, Object> context;

	public WorkflowContext() {
		super();
		context = new HashMap<String, Object>();
	}

	public Map<String, Object> getContext() {
		return context;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getReturnContext() {
		if (!context.containsKey(RETURN_CONTEXT)) {
			context.put(RETURN_CONTEXT, new HashMap<String, Object>());
		}
		return (Map<String, Object>) context.get(RETURN_CONTEXT);
	}
}
