package edu.clarkson.gdc.simulator;

public interface DataCenter extends CommunicationPoint {

	public String getId();

	public FailureStrategy getFailureStrategy();
}
