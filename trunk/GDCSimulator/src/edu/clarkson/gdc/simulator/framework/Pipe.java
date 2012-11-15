package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.clarkson.gdc.simulator.framework.DataEvent.PathNode;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public class Pipe extends Component {

	private Queue<DataEvent> buffer;

	public Pipe() {
		super();
		this.buffer = new ConcurrentLinkedQueue<DataEvent>();
	}

	public List<DataEvent> get() {
		List<DataEvent> output = new ArrayList<DataEvent>();
		while (true) {
			if (this.buffer.isEmpty())
				break;
			DataEvent event = this.buffer.peek();
			if (event.getTimestamp() <= getClock().getCounter())
				output.add(this.buffer.poll());
			else {
				break;
			}
		}
		return output;
	}

	public void put(DataEvent event) {
		event.access(new PathNode(this, getClock().getCounter() + getLatency()));
		buffer.offer(event);
	}

	@Override
	public void send() {
	}

	@Override
	public void process() {
	}
}
