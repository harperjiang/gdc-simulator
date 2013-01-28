package edu.clarkson.gdc.simulator.framework.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.EventListener;

public class EventListenerProxy implements InvocationHandler {

	private EventListenerDelegate delegate;

	public EventListenerProxy() {
		delegate = new EventListenerDelegate();
	}

	@SuppressWarnings("unchecked")
	public <E extends EventListener> E getProbe(Class<E> listenerClass) {
		return (E) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[] { listenerClass }, this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Class<? extends EventListener> declaring = (Class<? extends EventListener>) method
				.getDeclaringClass();
		for (Object listener : delegate.getListeners(declaring)) {
			method.invoke(listener, args);
		}
		return null;
	}

	public <EL extends EventListener> void addListener(Class<EL> clazz,
			EL listener) {
		delegate.addListener(clazz, listener);
	}

	public <EL extends EventListener> void removeListener(Class<EL> clazz,
			EL listener) {
		delegate.removeListener(clazz, listener);
	}
}
