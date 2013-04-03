package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.session.SessionManager;

public class ChainNodeTest {

	private TestNode source = new TestNode() {

		private SessionManager sm = new SessionManager();

		protected void processNew(MessageRecorder recorder) {
			DataMessage msg = new DataMessage();
			msg.setSessionId(sm.createSession().getId());
			msg.setLoad("Msg from Source");
			for (Pipe pipe : getPipes().values()) {
				recorder.record(pipe, msg);
			}
		}
	};

	private ChainNode middle = new ChainNode() {
		protected void processEach(Pipe pipe, DataMessage message,
				MessageRecorder recorder) {
			if (pipe.getOpponent(this).equals(source)) {
				DataMessage newmsg = new DataMessage();
				newmsg.setSessionId(message.getSessionId());
				recorder.record(getPipe(destination), newmsg);
			}
			if (pipe.getOpponent(this).equals(destination)) {
				ResponseMessage resp = new ResponseMessage(null) {
				};
				resp.setSessionId(message.getSessionId());
				recorder.record(getPipe(source), resp);
			}
		}
	};

	private Node destination = new Node() {
		@Override
		protected void processEach(Pipe pipe, DataMessage message,
				MessageRecorder recorder) {

			ResponseMessage rm = new ResponseMessage(message) {
			};
			rm.setSessionId(message.getSessionId());
			recorder.record(pipe, rm);
		}
	};

	@Test
	public void testChainNode() {
		Environment env = new Environment();
		env.add(source);
		env.add(middle);
		env.add(destination);
		new Pipe(source, middle);
		new Pipe(middle, destination);

		while (source.getFeedback().isEmpty()) {
			env.getClock().tick();
		}
		for (List<DataMessage> res : source.getFeedback().values()) {
			for (DataMessage dm : res) {
				assertTrue(dm instanceof ResponseMessage);
				ResponseMessage rm = (ResponseMessage) dm;
				assertNotNull(rm.getRequest());
				assertNotNull(rm.getSessionId());
				assertEquals(rm.getSessionId(), rm.getRequest().getSessionId());
				assertEquals("Msg from Source", rm.getRequest().getLoad());
			}
		}
		return;
	}

}
