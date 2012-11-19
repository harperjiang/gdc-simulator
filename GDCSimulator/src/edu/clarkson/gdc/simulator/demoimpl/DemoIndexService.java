package edu.clarkson.gdc.simulator.demoimpl;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.IndexService;

public class DemoIndexService implements IndexService {

	private DemoCloud cloud;

	public DemoCloud getCloud() {
		return cloud;
	}

	public void setCloud(DemoCloud cloud) {
		this.cloud = cloud;
	}

	@Override
	public String locate(String key, Object location) {
		try {
			// Record the access
			Object source = AccessStack.getInstance().peek();
			getCloud().fireAccessBetweenNodeEvent(source, this);
			AccessStack.getInstance().push(this);
			
			DataCenter dc = getCloud().getDataCenterByLocation(location);
			
			return dc.getId();
		} finally {
			AccessStack.getInstance().pop();
		}
	}

}
