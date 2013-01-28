package edu.clarkson.gdc.simulator.impl.client;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.NodeFailMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.DefaultClient;
import edu.clarkson.gdc.simulator.impl.WorkloadProvider;
import edu.clarkson.gdc.simulator.impl.message.LocateDCFail;
import edu.clarkson.gdc.simulator.impl.message.LocateDCRequest;
import edu.clarkson.gdc.simulator.impl.message.LocateDCResponse;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyRequest;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyResponse;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class RequestIndexClient extends DefaultClient {

	private Point2D location;

	public Point2D getLocation() {
		return location;
	}

	public void setLocation(Point2D location) {
		this.location = location;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected List<ProcessResult> process(Map<Pipe, List<DataMessage>> messages) {
		ProcessResult result = new ProcessResult();
		ProcessResult failed = new ProcessResult();

		List<ProcessResult> results = new ArrayList<ProcessResult>();

		Node indexService = (Node) ((Cloud) getEnvironment()).getIndexService();
		Pipe indexServicePipe = getPipe(indexService);
		// Send Query to Index Service
		List<String> keys = getProvider()
				.fetchReadLoad(getClock().getCounter());
		if (keys.size() > 0 && logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format(
					"{0} at tick {1}:Sending {2} LocateDC Message", getId(),
					getClock().getCounter(), keys.size()));
		}
		for (String key : keys) {
			LocateDCRequest locateDc = new LocateDCRequest(key, getLocation());
			result.add(indexServicePipe, locateDc);
		}
		// Process Income Messages
		for (Entry<Pipe, List<DataMessage>> entry : messages.entrySet()) {
			Pipe pipe = entry.getKey();

			// Get Response from Index Service, and send request to Data Center
			if (pipe.equals(indexServicePipe)) {
				List<DataMessage> indexServiceResp = entry.getValue();
				if (!CollectionUtils.isEmpty(indexServiceResp)) {
					for (DataMessage resp : indexServiceResp) {
						if (resp instanceof LocateDCFail) {
							fireFailure((LocateDCFail) resp);
							if (logger.isDebugEnabled()) {
								logger.debug(MessageFormat
										.format("{0} at tick {1} received LocateDC Failure",
												getId(), getClock()
														.getCounter()));
							}
						} else {
							LocateDCResponse ldcr = (LocateDCResponse) resp;

							fireSuccess(ldcr);
							String dcId = ldcr.getDataCenterId();
							Node dataCenter = (Node) ((Cloud) getEnvironment())
									.getDataCenter(dcId);
							LocateDCRequest request = (LocateDCRequest) ldcr
									.getRequest();
							ReadKeyRequest rkmsg = new ReadKeyRequest(
									request.getKey());
							result.add(getPipe(dataCenter), rkmsg);
							if (logger.isDebugEnabled()) {
								logger.debug(MessageFormat
										.format("{0} at tick {1} received LocateDC Success, goto DC {2}",
												getId(), getClock()
														.getCounter(), dcId));
							}
						}
					}
				}
			}
			// Collect Response from DataCenter
			if (pipe.getOpponent(this) instanceof DataCenter) {
				List<DataMessage> dcResponse = entry.getValue();
				for (DataMessage resp : dcResponse)
					if (resp instanceof NodeFailMessage) {
						fireFailure((NodeFailMessage) resp);
					} else {
						fireSuccess((ReadKeyResponse) resp);
					}
			}
		}

		results.add(result);
		results.add(failed);

		return results;
	}

	private WorkloadProvider provider;

	public WorkloadProvider getProvider() {
		return provider;
	}

	public void setProvider(WorkloadProvider provider) {
		this.provider = provider;
	}

}
