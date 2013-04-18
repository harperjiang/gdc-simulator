package edu.clarkson.gdc.simulator;

public interface DataCenter {

	public String getId();

	public <T> T getLocation();

	public ExceptionStrategy getExceptionStrategy();
}
