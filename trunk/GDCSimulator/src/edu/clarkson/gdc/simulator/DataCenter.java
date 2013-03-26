package edu.clarkson.gdc.simulator;

public interface DataCenter {

	public String getId();

	public Object getLocation();

	public ExceptionStrategy getExceptionStrategy();
}
