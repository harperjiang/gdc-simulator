package edu.clarkson.gdc.simulator.impl.stat;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.NodeMessageEvent;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.impl.message.LocateDCFail;
import edu.clarkson.gdc.simulator.impl.message.LocateDCResponse;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyResponse;
import edu.clarkson.gdc.simulator.stat.AvailabilityReport;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class StatisticListener implements NodeMessageListener {

	private AvailabilityReport report;

	public StatisticListener() {
		super();
		report = new AvailabilityReport();
	}

	@Override
	public void messageSent(NodeMessageEvent event) {
	}

	@Override
	public void messageReceived(NodeMessageEvent event) {
		if (event.getSource() instanceof Client
				&& event.getMessage() instanceof LocateDCFail) {
			report.setRequestCount(report.getRequestCount() + 1);
			report.setFailedCount(report.getFailedCount() + 1);
		}
		if (event.getSource() instanceof Client
				&& event.getMessage() instanceof LocateDCResponse)
			report.setRequestCount(report.getRequestCount() + 1);
		if (event.getSource() instanceof Client
				&& event.getMessage() instanceof ReadKeyResponse) {
			ResponseMessage response = (ResponseMessage) event.getMessage();
			report.setReadKeyCount(report.getReadKeyCount() + 1);
			report.setReadKeyTime(report.getReadKeyTime()
					+ response.getReceiveTime()
					- response.getRequest().getSendTime());
		}
	}

	public AvailabilityReport getReport() {
		return report;
	}

}
