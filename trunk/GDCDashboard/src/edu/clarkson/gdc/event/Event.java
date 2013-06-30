package edu.clarkson.gdc.event;

public interface Event {

	public String getType();

	public String getId();

	public Object getContent();
}
