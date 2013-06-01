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
import edu.clarkson.gdc.simulator.framework.NodeException;
import edu.clarkson.gdc.simulator.framework.NodeFailMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.module.client.AbstractClient;
import edu.clarkson.gdc.simulator.module.message.KeyException;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
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

		Node dc1 = new AbstractDataCenter() {

			int writeCount = 0;
			int readCount = 0;
			{
				setId("dc1");
			}

			@Override
			protected void processEach(Pipe source, DataMessage message,
					MessageRecorder recorder) {
				if (message instanceof KeyWrite) {
					switch (writeCount) {
					case 0:
						keydc1.add((((KeyWrite) message).getData().getKey()));
						recorder.record(source, new KeyResponse(message));
						break;
					case 1:
						recorder.record(source, new NodeFailMessage(message,
								new NodeException(this)));
						break;
					default:
						break;
					}
					writeCount++;
				} else {
					switch (readCount) {
					case 0:
						recorder.record(source, new KeyResponse(message));
						break;
					case 1:
						recorder.record(source, new NodeFailMessage(message,
								new NodeException(this)));
						break;
					default:
						break;
					}
					readCount++;
				}
			}
		};
		env.add(dc1);

		Node dc2 = new AbstractDataCenter() {

			int writeCount = 0;
			int readCount = 0;
			{
				setId("dc2");
			}

			@Override
			protected void processEach(Pipe source, DataMessage message,
					MessageRecorder recorder) {
				if (message instanceof KeyWrite) {
					switch (writeCount) {
					case 0:
						keydc2.add((((KeyWrite) message).getData().getKey()));
						recorder.record(source, new KeyResponse(message));
						break;
					case 1:
						recorder.record(source, new NodeFailMessage(message,
								new NodeException(this)));
						break;
					default:
						break;
					}
					writeCount++;
				}
				if (message instanceof KeyRead) {
					switch (readCount) {
					case 0:
					case 1:
						recorder.record(source, new NodeFailMessage(message,
								new KeyException(this,
										KeyException.READ_NOTFOUND)));
						break;
					default:
						break;
					}
					readCount++;
				}
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

					KeyRead read = new KeyRead();
					read.setKey("key2");
					recorder.record(40l, getServerPipe(), read);

					read = new KeyRead();
					read.setKey("key3");
					recorder.record(60l, getServerPipe(), read);

					recorder.record(80l, getServerPipe(), new KeyWrite(
							new DefaultData("key3")));

					read = new KeyRead();
					read.setKey("key2");
					recorder.record(100l, getServerPipe(), read);

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

		env.run(0);

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

		response.clear();
		while (response.isEmpty()) {
			env.getClock().tick();
		}
		message = response.get(0);
		assertTrue(message instanceof KeyResponse);

		response.clear();
		while (response.isEmpty()) {
			env.getClock().tick();
		}
		message = response.get(0);

		assertTrue(message instanceof FailMessage);
		assertTrue(((FailMessage) message).getException() instanceof KeyException);
		assertEquals(KeyException.READ_NOTFOUND,
				((KeyException) ((FailMessage) message).getException())
						.getError());

		gw.setWriteCopy(1);

		response.clear();
		while (response.isEmpty()) {
			env.getClock().tick();
		}
		message = response.get(0);
		assertTrue(message instanceof FailMessage);
		assertTrue(((FailMessage) message).getException() instanceof KeyException);
		assertEquals(KeyException.INTERNAL,
				((KeyException) ((FailMessage) message).getException())
						.getError());

		response.clear();
		while (response.isEmpty()) {
			env.getClock().tick();
		}
		message = response.get(0);
		assertTrue(message instanceof FailMessage);
		assertTrue(((FailMessage) message).getException() instanceof KeyException);
		assertEquals(KeyException.INTERNAL,
				((KeyException) ((FailMessage) message).getException())
						.getError());
	}
}
