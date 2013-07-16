package edu.clarkson.gdc.workflow;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.aop.TargetClassAware;

import edu.clarkson.gdc.workflow.param.Param;
import edu.clarkson.gdc.workflow.param.ParamParser;
import edu.clarkson.gdc.workflow.param.ParseException;

public class ExecutionUnit {

	static final String INPUT = "input";

	static final String CONTEXT = "context";

	static final String RETURN = "return";

	private String id;

	private Object bean;

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
		List<Object> results = new ArrayList<Object>();
		// Parse parameters
		try {
			List<Param> paramList = new ParamParser(new StringReader(
					getParams())).paramList();
			for (Param param : paramList) {
				results.add(param.getValue(inputs));
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return results;
	}

	protected Method findMethod(List<Object> paramList) throws Exception {
		Class<?> beanClass = null;
		if (bean instanceof TargetClassAware)
			beanClass = ((TargetClassAware) bean).getTargetClass();
		else
			beanClass = bean.getClass();
		Class<?>[] interfaces = beanClass.getInterfaces();
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
			for (Class<?> interf : interfaces) {
				try {
					return interf.getMethod(method, paramTypes);
				} catch (NoSuchMethodException e) {
				}
			}
		}
		for (Class<?> interf : interfaces) {
			for (Method m : interf.getMethods()) {
				if (this.method.equals(m.getName())
						&& m.getParameterTypes().length == paramList.size())
					return m;
			}
		}
		return null;
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
}
