package edu.clarkson.gdc.workflow;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

public class ExecutionUnit {

	static final String INPUT = "input";

	static final String CONTEXT = "context";

	static final String RETURN = "return";

	private String id;

	private Object bean;

	private Class<?> beanClass;

	private String method;

	private String params;

	private Method methodStub;

	public ExecutionUnit() {

	}

	public void execute(Object[] inputs) throws Exception {
		Validate.notNull(bean);
		Validate.notEmpty(method);

		List<Object> paramList = getParameters(inputs);

		if (null == methodStub) {
			methodStub = findMethod(paramList);
		}
		Object returnval = methodStub.invoke(bean, paramList.toArray());
		String key = getId();
		WorkflowContext.get().getReturnContext().put(key, returnval);
	}

	protected List<Object> getParameters(Object[] inputs) {
		// Parse parameters
		List<Object> paramList = new ArrayList<Object>();
		if (!StringUtils.isEmpty(params)) {
			for (String param : params.split(",")) {
				String[] parts = param.split(":");
				if (INPUT.equals(parts[0])) {
					int index = Integer.valueOf(parts[1]);
					paramList.add(inputs[index]);
				}
				if (CONTEXT.equals(parts[0])) {
					String key = parts[1];
					paramList.add(WorkflowContext.get().getContext().get(key));
				}
				if (RETURN.equals(parts[0])) {
					String key = parts[1];
					paramList.add(WorkflowContext.get().getReturnContext()
							.get(key));
				}
			}
		}
		return paramList;
	}

	protected Method findMethod(List<Object> paramList) throws Exception {
		Class<?>[] paramTypes = new Class<?>[paramList.size()];
		boolean byType = true;
		for (int i = 0; i < paramList.size(); i++) {
			if (paramList.get(i) != null)
				paramTypes[i] = paramList.get(i).getClass();
			else {
				byType = false;
				break;
			}
		}
		if (byType) {
			return beanClass.getMethod(method, paramTypes);
		} else {
			for (Method m : beanClass.getMethods()) {
				if (this.method.equals(m.getName())
						&& m.getParameterTypes().length == paramList.size())
					return m;
			}
			return null;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
}
