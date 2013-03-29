package edu.clarkson.gdc.simulator.framework;

import java.util.Stack;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public class DataMessage {

	private Stack<PathNode> paths;

	private Object load;

	private String sessionId;

	public static class PathNode {

		private long timestamp;

		private Component ref;

		public PathNode(Component ref, long timestamp) {
			super();
			this.ref = ref;
			this.timestamp = timestamp;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public Component getRef() {
			return ref;
		}

		public void setRef(Component ref) {
			this.ref = ref;
		}

	}

	public long getTimestamp() {
		if (paths.isEmpty())
			return -1;
		return paths.peek().getTimestamp();
	}

	public DataMessage() {
		super();
		this.paths = new Stack<PathNode>();
	}

	public void access(PathNode node) {
		this.paths.push(node);
	}

	public Object getLoad() {
		return load;
	}

	public void setLoad(Object load) {
		this.load = load;
	}

	public Component getOrigin() {
		return paths.get(0).getRef();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
