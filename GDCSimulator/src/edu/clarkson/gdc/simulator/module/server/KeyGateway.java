package edu.clarkson.gdc.simulator.module.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.Component;
import edu.clarkson.gdc.simulator.framework.DataDistribution;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.FailMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.session.Session;
import edu.clarkson.gdc.simulator.framework.session.SessionManager;
import edu.clarkson.gdc.simulator.framework.storage.OutOfSpaceException;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;

public class KeyGateway extends Node {

	public static final int UNLIMITED = Integer.MAX_VALUE;

	public static final String CLIENT_READ_COUNT = "client_read_count";

	public static final String CLIENT_WRITE_COUNT = "client_write_count";

	public static final String CLIENT_WRITE_REMAIN = "client_write_remain";

	private DataDistribution distribution;

	private SessionManager sessionManager;

	private Map<String, List<String>> keyToServer;

	private int writeCopy;

	public KeyGateway() {
		super();
		// Unlimited
		power = UNLIMITED;
		sessionManager = new SessionManager();

		keyToServer = new ConcurrentHashMap<String, List<String>>();
	}

	@Override
	protected void init() {
		if (null != distribution)
			distribution.init(getEnvironment(), getDataCenters());
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof KeyRead) {
			KeyRead read = (KeyRead) message;
			List<String> servers = keyToServer.get(read.getKey());
			if (CollectionUtils.isNotEmpty(servers)) {
				Session session = sessionManager.createSession(read
						.getSessionId());
				// Send request to each data center
				session.put(CLIENT_READ_COUNT, servers.size());
				for (String serverId : servers) {
					Pipe pipe = getPipe((Node) getEnvironment().getComponent(
							serverId));
					// Pass the read message to data center
					recorder.record(pipe, read);
				}
			} else {
				recorder.record(source, new KeyResponse(read,
						KeyResponse.READ_NOTFOUND, null));
			}
		}
		if (message instanceof KeyWrite) {
			KeyWrite write = (KeyWrite) message;
			List<String> servers = getDistribution().choose(
					write.getData().getKey());
			if (servers.size() < writeCopy) {
				recorder.record(source, new KeyResponse(write,
						KeyResponse.WRITE_NOTENOUGHCOPY, null));
			} else {
				Session session = sessionManager.createSession(write
						.getSessionId());
				session.put(CLIENT_WRITE_COUNT, 0);
				// Write to the first server
				String id = servers.remove(0);
				session.put(CLIENT_WRITE_REMAIN, servers);
				Pipe pipe = getPipe((Node) getEnvironment().getComponent(id));
				recorder.record(pipe, write);
			}
		}
		if (message instanceof FailMessage) {
			FailMessage fail = (FailMessage) message;
			if (fail.getRequest() instanceof KeyRead) {

			}
			if (fail.getRequest() instanceof KeyWrite) {

			}
		}
		if (message instanceof KeyResponse) {
			KeyResponse response = (KeyResponse) message;
		}
	}

	public DataDistribution getDistribution() {
		return distribution;
	}

	public void setDistribution(DataDistribution distribution) {
		this.distribution = distribution;
	}

	protected List<DataCenter> getDataCenters() {
		List<DataCenter> dcs = new ArrayList<DataCenter>();
		for (Component comp : getEnvironment().getComponents())
			if (comp instanceof DataCenter)
				dcs.add((DataCenter) comp);
		return dcs;
	}

	public int getWriteCopy() {
		return writeCopy;
	}

	public void setWriteCopy(int writeCopy) {
		this.writeCopy = writeCopy;
	}

}
