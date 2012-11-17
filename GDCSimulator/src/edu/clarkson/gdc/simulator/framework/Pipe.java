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

	private Queue<DataMessage> buffer;

	private Queue<ResponseMessage> responseBuffer;

	private Node source;

	private Node destination;

	public Pipe() {
		super();
		this.buffer = new ConcurrentLinkedQueue<DataMessage>();
		this.responseBuffer = new ConcurrentLinkedQueue<ResponseMessage>();
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
		while (true) {
			if (this.buffer.isEmpty())
				break;
			DataMessage event = this.buffer.peek();
			if (event.getTimestamp() <= getClock().getCounter())
				output.add(this.buffer.poll());
			else {
				break;
			}
		}
		return output;
	}

	// Asynchronous Event Sending
	public void put(Node sender, DataMessage event) {
		event.access(new PathNode(this, getClock().getCounter() + getLatency()));
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
