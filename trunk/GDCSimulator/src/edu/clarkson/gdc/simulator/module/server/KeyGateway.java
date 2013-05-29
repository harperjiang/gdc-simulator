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
import edu.clarkson.gdc.simulator.framework.NodeFailMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.session.Session;
import edu.clarkson.gdc.simulator.framework.session.SessionManager;
import edu.clarkson.gdc.simulator.module.message.KeyException;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;

public class KeyGateway extends Node {

	public static final int UNLIMITED = Integer.MAX_VALUE;

	public static final String PIPE = "pipe";

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
				session.put(PIPE, source);
				for (String serverId : servers) {
					Pipe pipe = getPipe((Node) getEnvironment().getComponent(
							serverId));
					// Pass the read message to data center
					recorder.record(pipe, read);
				}
			} else {
				recorder.record(source, new NodeFailMessage(read,
						new KeyException(this, KeyException.READ_NOTFOUND)));
			}
		}
		if (message instanceof KeyWrite) {
			KeyWrite write = (KeyWrite) message;
			List<String> servers = new ArrayList<String>();
			servers.addAll(getDistribution().choose(write.getData().getKey()));
			if (servers.size() < writeCopy) {
				recorder.record(source,
						new NodeFailMessage(write, new KeyException(this,
								KeyException.WRITE_NOTENOUGHCOPY)));
			} else {
				Session session = sessionManager.createSession(write
						.getSessionId());
				session.put(CLIENT_WRITE_COUNT, 0);
				session.put(PIPE, source);
				// Write to the first server
				String id = servers.remove(0);
				session.put(CLIENT_WRITE_REMAIN, servers);
				Pipe pipe = getPipe((Node) getEnvironment().getComponent(id));
				recorder.record(pipe, write);
			}
		}
		if (message instanceof FailMessage) {
			FailMessage fail = (FailMessage) message;
			Session session = sessionManager.getSession(fail.getSessionId());
			if (fail.getRequest() instanceof KeyRead && null != session) {
				// Decrease the wait count
				// If none successful message ever arrived
				int readCount = session.get(CLIENT_READ_COUNT);
				if (readCount == 1) {
					Pipe pipe = session.get(PIPE);
					recorder.record(pipe, fail);
					sessionManager.discardSession(session);
				} else {
					session.put(CLIENT_READ_COUNT, readCount - 1);
				}
			}

			if (fail.getRequest() instanceof KeyWrite && null != session) {
				// Send message to next server
				List<String> remain = session.get(CLIENT_WRITE_REMAIN);
				int success = session.get(CLIENT_WRITE_COUNT);
				if (remain.size() < getWriteCopy() - success) {
					// Cannot success, return fail
					Pipe pipe = session.get(PIPE);
					recorder.record(pipe, fail);
					sessionManager.discardSession(session);
				} else {
					String nextId = remain.remove(0);
					Pipe pipe = getPipe((Node) getEnvironment().getComponent(
							nextId));
					recorder.record(pipe, fail.getRequest());
				}
			}
		}
		if (message instanceof KeyResponse) {
			KeyResponse response = (KeyResponse) message;
			Session session = sessionManager
					.getSession(response.getSessionId());
			if (response.getRequest() instanceof KeyRead && null != session) {
				Pipe pipe = session.get(PIPE);
				recorder.record(pipe, new KeyResponse(response.getRequest()));
				sessionManager.discardSession(session);
			}
			if (response.getRequest() instanceof KeyWrite && null != session) {
				// Record key to server
				String key = ((KeyWrite) response.getRequest()).getData()
						.getKey();
				if (!keyToServer.containsKey(key)) {
					keyToServer.put(key, new ArrayList<String>());
				}
				keyToServer.get(key).add(response.getOrigin().getId());
				// Send next message
				int count = session.get(CLIENT_WRITE_COUNT);
				if (count >= getWriteCopy() - 1) {
					Pipe pipe = session.get(PIPE);
					recorder.record(pipe,
							new KeyResponse(response.getRequest()));
					sessionManager.discardSession(session);
				} else {
					session.put(CLIENT_WRITE_COUNT, count + 1);
					List<String> remain = session.get(CLIENT_WRITE_REMAIN);
					String nextId = remain.remove(0);
					Pipe pipe = getPipe((Node) getEnvironment().getComponent(
							nextId));
					recorder.record(pipe, response.getRequest());
				}
			}
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
