package edu.clarkson.gdc.simulator.storage;

public interface NestedStorage extends Storage {

	public Storage getInnerStorage();
}
