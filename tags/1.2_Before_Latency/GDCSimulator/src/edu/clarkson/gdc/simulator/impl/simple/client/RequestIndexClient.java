package edu.clarkson.gdc.simulator.impl.simple.client;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.NodeFailMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.AbstractClient;
import edu.clarkson.gdc.simulator.impl.simple.WorkloadProvider;
import edu.clarkson.gdc.simulator.impl.simple.message.LocateDCFail;
import edu.clarkson.gdc.simulator.impl.simple.message.LocateDCRequest;
import edu.clarkson.gdc.simulator.impl.simple.message.LocateDCResponse;
import edu.clarkson.gdc.simulator.impl.simple.message.ReadKeyRequest;
import edu.clarkson.gdc.simulator.impl.simple.message.ReadKeyResponse;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class RequestIndexClient extends AbstractClient {

	private Point2D location;

	public Point2D getLocation() {
		return location;
	}

	public void setLocation(Point2D location) {
		this.location = location;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Pipe indexServicePipe;

	protected void processNew(MessageRecorder recorder) {
		if (null == indexServicePipe) {
			Node indexService = (Node) ((Cloud) getEnvironment())
					.getIndexService();
			indexServicePipe = getPipe(indexService);
		}
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
			recorder.record(0l, indexServicePipe, locateDc);
		}
	}

	@Override
	protected void processEach(Pipe pipe, DataMessage message,
			MessageRecorder recorder) {

		// Process Income Messages

		// Get Response from Index Service, and send request to Data Center
		if (pipe.equals(indexServicePipe)) {

			if (message instanceof LocateDCFail) {
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format(
							"{0} at tick {1} received LocateDC Failure",
							getId(), getClock().getCounter()));
				}
			} else {
				LocateDCResponse ldcr = (LocateDCResponse) message;
				String dcId = ldcr.getDataCenterId();
				Node dataCenter = (Node) ((Cloud) getEnvironment())
						.getDataCenter(dcId);
				LocateDCRequest request = (LocateDCRequest) ldcr.getRequest();
				ReadKeyRequest rkmsg = new ReadKeyRequest(request.getKey());
				recorder.record(0l, getPipe(dataCenter), rkmsg);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat
							.format("{0} at tick {1} received LocateDC Success, goto DC {2}",
									getId(), getClock().getCounter(), dcId));
				}
			}
		}
		// Collect Response from DataCenter
		if (pipe.getOpponent(this) instanceof DataCenter) {
			if (message instanceof NodeFailMessage) {
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format(
							"{0} at tick {1} received ReadKey fail", getId(),
							getClock().getCounter()));
				}
			} else {
				ReadKeyRequest req = (ReadKeyRequest) ((ReadKeyResponse) message)
						.getRequest();
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat
							.format("{0} at tick {1} received ReadKey success for key {2} from DC {3}",
									getId(), getClock().getCounter(),
									req.getKey(), message.getOrigin().getId()));
				}
			}

		}
	}

	private WorkloadProvider provider;

	public WorkloadProvider getProvider() {
		return provider;
	}

	public void setProvider(WorkloadProvider provider) {
		this.provider = provider;
	}

}
