package edu.clarkson.gdc.event.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.common.MethodInvoker;
import edu.clarkson.gdc.event.Event;

public class BeanMethodHandler implements Handler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Object bean;

	private String method;

	private MethodInvoker invoker;

	@Override
	public void handle(Event event) {
		if (null == invoker)
			invoker = new MethodInvoker(bean, method);
		try {
			invoker.invoke(event.getContent());
		} catch (Exception e) {
			logger.error("Exception while invoking target method", e);
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
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

}
