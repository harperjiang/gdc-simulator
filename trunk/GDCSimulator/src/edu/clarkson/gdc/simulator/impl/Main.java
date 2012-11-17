package edu.clarkson.gdc.simulator.impl;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.demoimpl.DemoCloud;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = null;
		final Cloud cloud = new DemoCloud();

		client = new Client() {
			public void read() {
				String key = null;
				String location = null;
				String dcid = cloud.getIndexService().locate(key, location);
				DataCenter dc = cloud.getDataCenter(dcid);
				dc.read(key);
			}

			public void write() {

			}
		};

		Client client2 = new Client() {
			public void read() {
				String key = null;
				String location = null;
				DataCenter dc = cloud.getDataCenterByLocation(location);
				dc.read(key);
			}

			public void write() {

			}
		};
	}
}
