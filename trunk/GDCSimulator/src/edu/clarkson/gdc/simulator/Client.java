package edu.clarkson.gdc.simulator;

public interface Client {

	public CommunicationPoint getCommunicationPoint();

	public void read();

	public void write();
}
