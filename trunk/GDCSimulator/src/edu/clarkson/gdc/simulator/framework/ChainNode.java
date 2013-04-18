package edu.clarkson.gdc.simulator.framework;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import edu.clarkson.gdc.simulator.framework.session.Session;
import edu.clarkson.gdc.simulator.framework.session.SessionManager;

/**
 * <h4>Message Chaining</h4>
 * <p>
 * <code>ChainNode</code> has a built-in chaining support. If it receives a
 * message with a session id, this message will be recorded. If a response
 * message with the same session id is to be sent out, the original recorded
 * message will be removed, and will be returned as
 * {@link ResponseMessage#getRequest()}
 * </p>
 * 
 * @author Harper
 * 
 */
public abstract class ChainNode extends Node {

	private static final String MESSAGE = "MESSAGE";

	private static final String PIPE = "PIPE";

	private SessionManager sessionManager;

	public ChainNode() {
		super();
		sessionManager = new SessionManager();
	}

	/*
	 * Callback, check message with session ids, save to session
	 */
	protected void beforeProcess(Map<Pipe, List<DataMessage>> events) {
		for (Entry<Pipe, List<DataMessage>> entry : events.entrySet()) {
			List<DataMessage> msgs = entry.getValue();
			for (DataMessage message : msgs) {
				if (message instanceof DataMessage
						&& !(message instanceof ResponseMessage)
						&& !StringUtils.isEmpty(message.getSessionId())) {
					Session session = sessionManager.createSession(message
							.getSessionId());
					session.put(MESSAGE, message);
					session.put(PIPE, entry.getKey());
				}
			}
		}
	}

	/*
	 * Callback, check response with session ids
	 */
	protected void afterProcess(Map<Pipe, List<DataMessage>> events,
			List<ProcessResult> results) {
		for (ProcessResult result : results) {
			DataMessage message = result.getMessage();
			if (message instanceof ResponseMessage
					&& !StringUtils.isEmpty(message.getSessionId())) {
				Session session = sessionManager.getSession(message
						.getSessionId());
				if (null != session) {
					if (((ResponseMessage) message).getRequest() == null) {
						DataMessage request = session.get(MESSAGE);
						((ResponseMessage) message).setRequest(request);
						message.setSessionId(session.getId());
					}
					sessionManager.discardSession(session);
				}
			}
		}
	}

	protected Pipe getPipe(String uuid) {
		return sessionManager.getSession(uuid).get(PIPE);
	}
}
