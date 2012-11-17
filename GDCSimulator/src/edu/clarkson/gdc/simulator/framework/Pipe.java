package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.clarkson.gdc.simulator.framework.DataMessage.PathNode;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public class Pipe extends Component {

	private Queue<DataMessage> requestBuffer;

	private Queue<DataMessage> responseBuffer;

	private Node source;

	private Node destination;

	public Pipe() {
		super();
		this.requestBuffer = new ConcurrentLinkedQueue<DataMessage>();
		this.responseBuffer = new ConcurrentLinkedQueue<DataMessage>();
	}

	public Pipe(Node source, Node destination) {
		this();
		this.source = source;
		this.destination = destination;
		// Build Connections
		this.source.addPipe(this);
		this.destination.addPipe(this);
	}

	public List<DataMessage> get(Node receiver) {
		List<DataMessage> output = new ArrayList<DataMessage>();
		Queue<DataMessage> buffer = (receiver == destination) ? requestBuffer
				: responseBuffer;
		while (true) {
			if (buffer.isEmpty())
				break;
			DataMessage event = buffer.peek();
			if (event.getTimestamp() <= getClock().getCounter())
				output.add(buffer.poll());
			else {
				break;
			}
		}
		return output;
	}

	// Asynchronous Event Sending
	public void put(Node sender, DataMessage event) {
		event.access(new PathNode(this, getClock().getCounter() + getLatency()));
		Queue<DataMessage> buffer = (sender == source) ? requestBuffer
				: responseBuffer;
		buffer.offer(event);
	}

	public Node getSource() {
		return source;
	}

	public Node getDestination() {
		return destination;
	}

	@Override
	public void send() {
	}

	@Override
	public void process() {
	}
}
