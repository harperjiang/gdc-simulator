package edu.clarkson.gdc.simulator;

import javax.swing.event.EventListenerList;

import edu.clarkson.gdc.simulator.event.SimulatorEvent;
import edu.clarkson.gdc.simulator.event.SimulatorEventListener;
import edu.clarkson.gdc.simulator.event.SimulatorEventType;
import edu.clarkson.gdc.simulator.event.Tuple;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = null;
		final Cloud cloud = new Cloud() {

			private EventListenerList listenerList;

			public void addSimulatorEventListener(
					SimulatorEventListener listener) {
				listenerList.add(SimulatorEventListener.class, listener);
			}

			public void removeSimulatorEventListener(
					SimulatorEventListener listener) {
				listenerList.remove(SimulatorEventListener.class, listener);
			}

			protected void fireAcessBetweenNodes(Object source, Object dest) {
				Tuple tupleSource = new Tuple(source, dest);
				SimulatorEvent event = new SimulatorEvent(tupleSource);
				event.setType(SimulatorEventType.ACCESS);
				for (SimulatorEventListener listener : listenerList
						.getListeners(SimulatorEventListener.class)) {
					listener.accessBetweenNodes(event);
				}
			}
			
			@Override
			public DataCenter getDataCenter(String dcid) {
				fireAcessBetweenNodes(indexService, dc1);
				return null;
			}
		};
		IndexService is = cloud.getIndexService();

		client = new Client() {
			public void read() {
				String key = null;
				String location = null;
				String dcid = cloud.getIndexService().locate(key, location);
				DataCenter dc = cloud.getDataCenter(dcid);
				dc.read(key);
			}

			public void write() {

			}
		};

		Client client2 = new Client() {
			public void read() {
				String key = null;
				String location = null;
				DataCenter dc = cloud.getDataCenterByLocation(location);
				dc.read(key);
			}

			public void write() {

			}
		};
	}
}
