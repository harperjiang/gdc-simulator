package edu.clarkson.gdc.simulator.scenario.workload.readmywrite;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;

public class ReadMyWriteClientTest {

	@Test
	public void testClient() {
		ReadMyWriteClient client = new ReadMyWriteClient(5, 0.5f, 2, 10000);

		final Set<String> written = new HashSet<String>();

		Node receiver = new Node() {
			@Override
			protected void processEach(Pipe source, DataMessage message,
					MessageRecorder recorder) {
				if (message instanceof KeyWrite) {
					written.add(((KeyWrite) message).getData().getKey());
					recorder.record(source, new KeyResponse((KeyWrite) message));
				} else if (message instanceof KeyRead) {
					if (!written.contains(((KeyRead) message).getKey())) {
						Assert.fail("DDD");
					} else {
						recorder.record(source, new KeyResponse(
								(KeyRead) message));
					}
				} else {
					Assert.fail("DDD");
				}
			}
		};
		Environment env = new Environment();
		env.add(client);
		env.add(receiver);
		new Pipe(client, receiver);

		env.run(100000);

	}
}
