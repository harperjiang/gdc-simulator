package edu.clarkson.gdc.simulator.impl;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.IndexService;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.message.LocateDCRequest;
import edu.clarkson.gdc.simulator.impl.message.LocateDCResponse;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class DefaultIndexService extends Node implements IndexService {

	@Override
	public String locate(String key, Object location) {
		Point2D loc = (Point2D) location;

		if (StringUtils.isEmpty(key)) {
			return DefaultCloud.getInstance().getDataCenterByLocation(location)
					.getId();
		} else {
			// Get Distribution, compare the location
			List<String> dcids = DefaultCloud.getInstance()
					.getDataBlockDistribution().locate(key);
			DataCenter minDc = null;
			Double minVal = Double.MAX_VALUE;
			for (String dcid : dcids) {
				DataCenter currentDc = DefaultCloud.getInstance()
						.getDataCenter(dcid);
				Point2D currentLoc = (Point2D) currentDc.getLocation();
				Double length = currentLoc.distance(loc);
				if (length < minVal) {
					minVal = length;
					minDc = currentDc;
				}
			}
			return minDc.getId();
		}
	}

	@Override
	protected ProcessGroup process(Map<Pipe, List<DataMessage>> events) {
		ProcessResult success = new ProcessResult();
		ProcessResult failed = new ProcessResult();
		ProcessGroup group = new ProcessGroup(success, failed);

		for (Entry<Pipe, List<DataMessage>> entry : events.entrySet()) {
			Pipe pipe = entry.getKey();
			for (DataMessage message : entry.getValue()) {
				if (message instanceof LocateDCRequest) {
					LocateDCRequest ldcr = (LocateDCRequest) message;
					String key = ldcr.getKey();
					Point2D location = ldcr.getClientLoc();
					String dcid = locate(key, location);

					LocateDCResponse response = new LocateDCResponse(ldcr, dcid);

					success.add(pipe, response);
				}
			}
		}

		return group;
	}
}
