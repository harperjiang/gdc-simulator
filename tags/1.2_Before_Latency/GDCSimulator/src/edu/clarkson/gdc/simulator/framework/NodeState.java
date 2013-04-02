package edu.clarkson.gdc.simulator.framework;

import java.util.EventListener;

import edu.clarkson.gdc.simulator.framework.utils.EventListenerDelegate;

public enum NodeState {

	FREE, BUSY, DOWN, EXCEPTION;

	public static final class NodeStateMachine {

		private NodeState currentState = FREE;

		private EventListenerDelegate listenerDelegate = new EventListenerDelegate();

		private Node source;

		public NodeStateMachine(Node source) {
			super();
			this.source = source;
		}

		public NodeState getState() {
			return currentState;
		}

		protected void setState(NodeState newState) {
			NodeState oldState = getState();
			this.currentState = newState;
			if (oldState != currentState) {
				fireStateChanged(oldState, currentState);
			}
		}

		public void tick() {
			if (counter != 0)
				counter--;
			if (counter == 0 && currentState == BUSY) {
				setState(FREE);
			}
		}

		private long counter;

		public void busy(long period) {
			if (getState() != NodeState.FREE)
				return;
			if (period > 0) {
				counter = period;
				setState(BUSY);
			}
		}

		private NodeException exception;

		public void exception(NodeException e) {
			if (getState() != DOWN)
				setState(EXCEPTION);
			this.exception = e;
		}

		public NodeException getException() {
			if (getState() == EXCEPTION)
				return this.exception;
			return null;
		}

		public void free() {
			setState(FREE);
		}

		public void shutdown() {
			setState(DOWN);
		}

		protected void fireStateChanged(NodeState from, NodeState to) {
			NodeStateEvent event = new NodeStateEvent(source, from, to);
			source.fireStateChange(event);
		}

		public <EL extends EventListener> void addListener(Class<EL> clazz,
				EL listener) {
			listenerDelegate.addListener(clazz, listener);
		}

		public <EL extends EventListener> EL[] getListeners(
				Class<EL> listenerClass) {
			return listenerDelegate.getListeners(listenerClass);
		}
	}
}
