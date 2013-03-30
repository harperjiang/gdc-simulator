package edu.clarkson.gdc.simulator.framework.storage;

public interface NestedStorage extends Storage {

	public Storage getInnerStorage();
}
