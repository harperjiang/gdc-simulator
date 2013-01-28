package edu.clarkson.gdc.simulator.framework;

import java.util.EventListener;

import edu.clarkson.gdc.simulator.framework.utils.EventListenerDelegate;

public enum NodeState {

	FREE, BUSY, DOWN, EXCEPTION;

	public static final class NodeStateMachine {

		private NodeState currentState = FREE;

		private EventListenerDelegate listenerDelegate = new EventListenerDelegate();

		public NodeState getState() {
			return currentState;
		}

		public void tick() {
			if (counter != 0)
				counter--;
			if (counter == 0 && currentState == BUSY) {
				currentState = FREE;
			}
		}

		private long counter;

		public void busy(long period) {
			counter = period;
			currentState = BUSY;
		}

		private NodeException exception;

		public void exception(NodeException e) {
			if (currentState != DOWN)
				currentState = EXCEPTION;
			this.exception = e;
		}

		public NodeException getException() {
			if (currentState == EXCEPTION)
				return this.exception;
			return null;
		}

		public void free() {
			currentState = FREE;
		}

		public void shutdown() {
			currentState = DOWN;
		}

		protected void fireStateChanged(NodeState from, NodeState to) {
			NodeStateEvent event = new NodeStateEvent(null, from, to);
			for (NodeStateListener listener : listenerDelegate
					.getListeners(NodeStateListener.class)) {
				listener.stateChanged(event);
			}
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
