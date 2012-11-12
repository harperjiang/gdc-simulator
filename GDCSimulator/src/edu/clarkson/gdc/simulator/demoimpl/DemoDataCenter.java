package edu.clarkson.gdc.simulator.demoimpl;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.FailureStrategy;

public class DemoDataCenter implements DataCenter {

	private DemoCloud cloud;

	public DemoCloud getCloud() {
		return cloud;
	}

	public void setCloud(DemoCloud cloud) {
		this.cloud = cloud;
	}

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private DemoFailureStrategy failureStrategy;

	@Override
	public FailureStrategy getFailureStrategy() {
		return failureStrategy;
	}

	@Override
	public Data read(String key) {
		Object prev = AccessStack.getInstance().peek();
		AccessStack.getInstance().push(this);
		getCloud().fireAccessBetweenNodeEvent(prev, this);
		try {
			if (getFailureStrategy().shouldFail()) {
				getCloud().fireServerFailedEvent(this);
				return null;
			} else {
				return new DemoData(key);
			}
		} finally {
			AccessStack.getInstance().pop();
		}
	}

	@Override
	public void write(Data data) {
		throw new UnsupportedOperationException();
	}
}
