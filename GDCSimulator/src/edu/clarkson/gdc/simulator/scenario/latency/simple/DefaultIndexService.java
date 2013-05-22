package edu.clarkson.gdc.simulator.scenario.latency.simple;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.IndexService;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.scenario.latency.simple.message.LocateDCFail;
import edu.clarkson.gdc.simulator.scenario.latency.simple.message.LocateDCRequest;
import edu.clarkson.gdc.simulator.scenario.latency.simple.message.LocateDCResponse;

/**
 * 
 * This Index Service knows which server is available. So basically you will
 * never go to a data center that is not available
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class DefaultIndexService extends Node implements IndexService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String locate(String key, Object location) {
		Point2D loc = (Point2D) location;

		if (StringUtils.isEmpty(key)) {
			return ((DefaultCloud) getEnvironment()).getDataCenterByLocation(
					location).getId();
		} else {
			// Get Distribution, compare the location
			List<String> dcids = ((DefaultCloud) getEnvironment())
					.getDataBlockDistribution().locate(key);
			DataCenter minDc = null;
			Double minVal = Double.MAX_VALUE;
			for (String dcid : dcids) {
				DataCenter currentDc = ((Cloud) getEnvironment())
						.getDataCenter(dcid);
				// Ignore Failed Data Center
				if (null != currentDc.getExceptionStrategy().getException(
						getClock().getCounter()))
					continue;
				Point2D currentLoc = (Point2D) currentDc.getLocation();
				Double length = currentLoc.distance(loc);
				if (length < minVal) {
					minVal = length;
					minDc = currentDc;
				}
			}
			if (null == minDc)
				return null;
			return minDc.getId();
		}
	}

	@Override
	protected void processEach(Pipe pipe, DataMessage message,
			MessageRecorder recorder) {

		if (message instanceof LocateDCRequest) {
			LocateDCRequest ldcr = (LocateDCRequest) message;
			String key = ldcr.getKey();
			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format(
						"{0} at tick {1} received request to locate key {2}",
						getId(), getClock().getCounter(), key));
			}
			Point2D location = ldcr.getClientLoc();
			String dcid = locate(key, location);
			if (null == dcid) {
				LocateDCFail fail = new LocateDCFail(ldcr);
				recorder.record(0l, pipe, fail);
			} else {
				LocateDCResponse response = new LocateDCResponse(ldcr, dcid);
				recorder.record(0l, pipe, response);
			}
		}
	}
}
