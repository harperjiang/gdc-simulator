package edu.clarkson.gdc.event;

public interface EventSubscriber {

	public EventSelector getSelector();

	public void onEvent(Event event);
}
