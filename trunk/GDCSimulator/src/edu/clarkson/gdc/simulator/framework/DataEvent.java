package edu.clarkson.gdc.simulator.framework;

import java.util.Stack;

public class DataEvent {

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
		return paths.peek().getTimestamp();
	}

	private Stack<PathNode> paths;

	public DataEvent() {
		super();
		this.paths = new Stack<PathNode>();
	}
	
	public void access(PathNode node) {
		this.paths.push(node);
	}
}
