package edu.clarkson.gdc.simulator.framework.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

	private Map<String, Session> sessions = new HashMap<String, Session>();

	public Session createSession() {
		return createSession(UUID.randomUUID().toString());
	}

	public Session createSession(String uuid) {
		Session session = new Session();
		session.setUuid(uuid);
		sessions.put(session.getUuid(), session);
		return session;
	}

	public Session getSession(String uuid) {
		return sessions.get(uuid);
	}

	public void discardSession(Session session) {
		sessions.remove(session.getUuid());
	}
}
