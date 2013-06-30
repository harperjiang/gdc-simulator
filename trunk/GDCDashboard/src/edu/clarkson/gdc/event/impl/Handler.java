package edu.clarkson.gdc.event.impl;

import edu.clarkson.gdc.event.Event;

public interface Handler {

	public void handle(Event event);
}
