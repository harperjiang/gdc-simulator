package edu.clarkson.gdc.simulator.impl.stat;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeListener;
import edu.clarkson.gdc.simulator.framework.NodeResponseEvent;
import edu.clarkson.gdc.simulator.impl.message.LocateDCFail;
import edu.clarkson.gdc.simulator.impl.message.LocateDCResponse;
import edu.clarkson.gdc.simulator.stat.AvailabilityReport;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class StatisticListener implements NodeListener {

	private AvailabilityReport report;

	public StatisticListener() {
		super();
		report = new AvailabilityReport();
	}

	@Override
	public void successReceived(NodeResponseEvent event) {
		if (event.getSource() instanceof Client
				&& event.getMessage() instanceof LocateDCResponse)
			report.setRequestCount(report.getRequestCount() + 1);
	}

	@Override
	public void failureReceived(NodeResponseEvent event) {
		if (event.getSource() instanceof Client
				&& event.getMessage() instanceof LocateDCFail) {
			report.setRequestCount(report.getRequestCount() + 1);
			report.setFailedCount(report.getFailedCount() + 1);
		}
	}

	public AvailabilityReport getReport() {
		return report;
	}

}
