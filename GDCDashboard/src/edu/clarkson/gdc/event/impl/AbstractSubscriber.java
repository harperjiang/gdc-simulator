package edu.clarkson.gdc.event.impl;

import edu.clarkson.gdc.event.EventSubscriber;

public abstract class AbstractSubscriber implements EventSubscriber {

	private EventMediator mediator;

	public EventMediator getMediator() {
		return mediator;
	}

	public void setMediator(EventMediator mediator) {
		this.mediator = mediator;
		this.mediator.addSubscriber(this);
	}

}
