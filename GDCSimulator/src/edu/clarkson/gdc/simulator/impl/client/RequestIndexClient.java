package edu.clarkson.gdc.simulator.impl.client;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.DefaultCloud;
import edu.clarkson.gdc.simulator.impl.message.LocateDCFail;
import edu.clarkson.gdc.simulator.impl.message.LocateDCRequest;
import edu.clarkson.gdc.simulator.impl.message.LocateDCResponse;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyFail;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyRequest;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyResponse;

public class RequestIndexClient extends Node implements Client {

	@Override
	public void read() {

	}

	@Override
	public void write() {

	}

	@Override
	protected ProcessGroup process(Map<Pipe, List<DataMessage>> messages) {
		ProcessResult result = new ProcessResult();
		ProcessResult failed = new ProcessResult();

		ProcessGroup group = new ProcessGroup(result, failed);

		// Send Query to Index Service
		// TODO Find key somewhere, and limit the request sending count
		String key = null;
		LocateDCRequest readKey = new LocateDCRequest(key);
		Node indexService = (Node) DefaultCloud.getInstance().getIndexService();
		Pipe indexServicePipe = getPipe(indexService);
		result.add(indexServicePipe, readKey);
		// Process Income Messages
		for (Entry<Pipe, List<DataMessage>> entry : result.getMessages()
				.entrySet()) {
			Pipe pipe = entry.getKey();

			// Get Response from Index Service, and send request to Data Center
			if (pipe.equals(indexServicePipe)) {
				List<DataMessage> indexServiceResp = entry.getValue();
				if (!CollectionUtils.isEmpty(indexServiceResp)) {
					for (DataMessage resp : indexServiceResp) {
						if (resp instanceof LocateDCFail) {
							reportFailure((LocateDCFail) resp);
						} else {
							LocateDCResponse ldcr = (LocateDCResponse) resp;
							reportSuccess(ldcr);
							String dcId = ldcr.getDataCenterId();
							Node dataCenter = (Node) DefaultCloud.getInstance()
									.getDataCenter(dcId);
							LocateDCRequest request = (LocateDCRequest) ldcr
									.getRequest();
							ReadKeyRequest rkmsg = new ReadKeyRequest(
									request.getKey());
							result.add(getPipe(dataCenter), rkmsg);
						}
					}
				}
			}
			// Collect Response from DataCenter
			if (pipe.getOpponent(this) instanceof DataCenter) {
				List<DataMessage> dcResponse = entry.getValue();
				for (DataMessage resp : dcResponse)
					if (resp instanceof ReadKeyFail) {
						reportFailure((ReadKeyFail) resp);
					} else {
						reportSuccess((ReadKeyResponse) resp);
					}
			}
		}
		return group;
	}
}
