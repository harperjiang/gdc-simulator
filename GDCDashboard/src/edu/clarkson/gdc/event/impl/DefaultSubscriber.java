package edu.clarkson.gdc.event.impl;

import edu.clarkson.gdc.event.Event;
import edu.clarkson.gdc.event.EventSelector;

public class DefaultSubscriber extends AbstractSubscriber {

	private EventSelector selector;

	private Handler handler;

	@Override
	public void onEvent(Event event) {
		getHandler().handle(event);
	}

	public EventSelector getSelector() {
		return selector;
	}

	public void setSelector(EventSelector selector) {
		this.selector = selector;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
