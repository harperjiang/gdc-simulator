package edu.clarkson.gdc.simulator;

public interface DataCenter {

	public String getId();
	
	public Object getLocation();

	public FailureStrategy getFailureStrategy();

	public Data read(String key);

	public void write(Data data);
}
