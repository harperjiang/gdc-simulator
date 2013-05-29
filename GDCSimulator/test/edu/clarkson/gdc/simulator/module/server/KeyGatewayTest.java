package edu.clarkson.gdc.simulator.module.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.DataDistribution;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.FailMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.module.client.AbstractClient;
import edu.clarkson.gdc.simulator.module.message.KeyException;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultData;

public class KeyGatewayTest {

	@Test
	public void testKeyGateway() {
		final List<DataMessage> response = new ArrayList<DataMessage>();
		final Set<String> keydc1 = new HashSet<String>();
		final Set<String> keydc2 = new HashSet<String>();
		Environment env = new Environment();

		KeyGateway gw = new KeyGateway();
		gw.setDistribution(new DataDistribution() {

			private List<String> dcs;
			{
				dcs = new ArrayList<String>();
				dcs.add("dc1");
				dcs.add("dc2");
			}

			@Override
			public void init(Environment env, List<DataCenter> dcs) {

			}

			@Override
			public List<String> choose(String key) {
				return dcs;
			}

			@Override
			public List<String> locate(String key) {
				return dcs;
			}
		});
		env.add(gw);

		Node dc1 = new Node() {
			{
				setId("dc1");
			}

			@Override
			protected void processEach(Pipe source, DataMessage message,
					MessageRecorder recorder) {
				if (message instanceof KeyWrite) {
					keydc1.add((((KeyWrite) message).getData().getKey()));
				}
				recorder.record(source, new KeyResponse(message));
			}
		};
		env.add(dc1);

		Node dc2 = new Node() {
			{
				setId("dc2");
			}

			@Override
			protected void processEach(Pipe source, DataMessage message,
					MessageRecorder recorder) {
				if (message instanceof KeyWrite) {
					keydc2.add((((KeyWrite) message).getData().getKey()));
				}
				recorder.record(source, new KeyResponse(message));
			}
		};
		env.add(dc2);

		AbstractClient client = new AbstractClient() {
			{
				setPower(1);
			}

			@Override
			protected void processNew(MessageRecorder recorder) {
				if (getEnvironment().getClock().getCounter() == 2) {
					recorder.record(1l, getServerPipe(), new KeyWrite(
							new DefaultData("key1")));
					recorder.record(20l, getServerPipe(), new KeyWrite(
							new DefaultData("key2")));
				}
			}

			@Override
			protected void processEach(Pipe source, DataMessage message,
					MessageRecorder recorder) {
				response.add(message);
			}

			protected Pipe getServerPipe() {
				Iterator<Pipe> it = getPipes().values().iterator();
				while (it.hasNext()) {
					Pipe pipe = it.next();
					if (pipe.getOpponent(this) != this) {
						return pipe;
					}
				}
				return null;
			}
		};
		env.add(client);

		new Pipe(client, gw);
		new Pipe(gw, dc1);
		new Pipe(gw, dc2);

		gw.setWriteCopy(3);

		while (response.isEmpty()) {
			env.getClock().tick();
		}
		DataMessage message = response.get(0);

		assertTrue(message instanceof FailMessage);
		assertTrue(((FailMessage) message).getException() instanceof KeyException);
		assertEquals(KeyException.WRITE_NOTENOUGHCOPY,
				((KeyException) ((FailMessage) message).getException())
						.getError());

		gw.setWriteCopy(2);
		response.clear();
		while (response.isEmpty()) {
			env.getClock().tick();
		}
		message = response.get(0);
		assertTrue(message instanceof KeyResponse);
		assertTrue(keydc1.contains("key2"));
		assertTrue(keydc2.contains("key2"));
	}
}
