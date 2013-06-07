package edu.clarkson.gdc.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHolder {

	private ApplicationContext applicationContext;

	private ApplicationContextHolder() {

	}

	private static ApplicationContextHolder instance = new ApplicationContextHolder();

	public static ApplicationContextHolder getInstance() {
		return instance;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public static class Adaptor implements ApplicationContextAware {

		public void setApplicationContext(ApplicationContext arg0)
				throws BeansException {
			ApplicationContextHolder.getInstance().setApplicationContext(arg0);
		}
	}
}
