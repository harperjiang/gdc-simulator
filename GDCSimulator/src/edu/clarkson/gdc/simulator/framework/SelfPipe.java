package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.framework.DataMessage.PathNode;

public class SelfPipe extends Pipe {

	public SelfPipe(Node owner) {
		super();
		this.source = owner;
		this.destination = owner;
		source.addPipe(this);

		setId(source.getId() + "--");
	}

	public List<DataMessage> get() {
		return get(source);
	}

	public void put(DataMessage event) {
		put(source, event);
	}

	@Override
	public List<DataMessage> get(Node receiver) {
		Validate.isTrue(receiver == source);
		List<DataMessage> output = new ArrayList<DataMessage>();
		Queue<DataMessage> buffer = requestBuffer;
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

	@Override
	public void put(Node sender, DataMessage event) {
		Validate.isTrue(sender == source);
		event.access(new PathNode(this, getClock().getCounter()
				+ getLatency(null)));
		Queue<DataMessage> buffer = requestBuffer;
		buffer.offer(event);
	}
}
