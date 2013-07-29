package edu.clarkson.gdc.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceErrorLogger implements MethodInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
		} catch (RuntimeException e) {
			logger.error("Unhandled exception captured in service", e);
			throw e;
		}
	}
}
