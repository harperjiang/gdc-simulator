package edu.clarkson.cs.gdc.network.ipquery.task;

import edu.clarkson.cs.gdc.network.Service;
import edu.clarkson.cs.gdc.network.ipquery.api.QueryIPInfoResponse;
import edu.clarkson.cs.gdc.network.ipquery.model.IPInfo;

public class QueryIPInfo {

	public static void main(String[] args) throws Exception {
		QueryIPInfoResponse resp = new Service().ipinfo()
				.queryip("128.153.23.185").execute();

		IPInfo info = resp.getResult();
		return;
	}

}
