package edu.clarkson.gdc.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.aop.TargetClassAware;

public class MethodInvoker {

	private Object bean;

	private String method;

	private Method methodStub;

	public MethodInvoker(Object bean, String method) {
		this.bean = bean;
		this.method = method;
	}

	public Object invoke(Object... params) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (null == methodStub) {
			methodStub = findMethod();
		}
		return methodStub.invoke(bean, params);
	}

	protected Method findMethod() {
		Class<?> beanClass = null;
		if (bean instanceof TargetClassAware) {
			beanClass = ((TargetClassAware) bean).getTargetClass();
		} else {
			beanClass = bean.getClass();
		}
		Class<?>[] interfaces = beanClass.getInterfaces();
		for (Class<?> interf : interfaces) {
			for (Method m : interf.getMethods()) {
				if (this.method.equals(m.getName()))
					return m;
			}
		}
		return null;
	}
}
