package edu.clarkson.cs.gdc.network.tracedata.task;

import org.eclipse.persistence.queries.ScrollableCursor;

import edu.clarkson.cs.gdc.network.Service;
import edu.clarkson.cs.gdc.network.tracedata.api.TraceDataDao;
import edu.clarkson.cs.gdc.network.tracedata.model.TraceData;

public class QueryTraceDataInfo {

	public static void main(String[] args) {
		Service service = new Service();

		TraceDataDao tdd = new TraceDataDao();
		ScrollableCursor cursor = tdd.allData();
		while (cursor.hasMoreElements()) {
			TraceData td = (TraceData) cursor.next();
			service.ipinfo().getInfo(td.getSourceIp());
			service.ipinfo().getInfo(td.getFromIp());
			service.ipinfo().getInfo(td.getToIp());
		}
	}

}
