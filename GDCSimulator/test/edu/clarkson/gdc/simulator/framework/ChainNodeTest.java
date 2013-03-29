package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.session.SessionManager;

public class ChainNodeTest {

	private TestNode source = new TestNode() {

		private SessionManager sm = new SessionManager();

		protected List<ProcessResult> process(
				Map<Pipe, List<DataMessage>> events) {
			List<ProcessResult> result = super.process(events);
			if (null == result) {
				result = new ArrayList<ProcessResult>();
			}
			// Emit message

			ProcessResult pr = new ProcessResult();
			// send out immediately
			pr.setTimestamp(getClock().getCounter());

			DataMessage msg = new DataMessage();
			msg.setSessionId(sm.createSession().getId());
			msg.setLoad("Msg from Source");
			for (Pipe pipe : getPipes().values()) {
				pr.add(pipe, msg);
			}

			result.add(pr);

			return result;
		};
	};

	private ChainNode middle = new ChainNode() {
		protected List<ProcessResult> process(
				Map<Pipe, List<DataMessage>> events) {

			List<ProcessResult> result = new ArrayList<ProcessResult>();
			ProcessResult pr = new ProcessResult();
			result.add(pr);

			// Generate a new message for every coming one, and send to dest
			for (Entry<Pipe, List<DataMessage>> entry : events.entrySet()) {
				if (entry.getKey().getOpponent(this).equals(source)) {
					for (DataMessage msg : entry.getValue()) {
						DataMessage newmsg = new DataMessage();
						newmsg.setSessionId(msg.getSessionId());
						pr.add(getPipe(destination), newmsg);
					}
				}
				if (entry.getKey().getOpponent(this).equals(destination)) {
					for (DataMessage rm : entry.getValue()) {
						ResponseMessage resp = new ResponseMessage(null) {
						};
						resp.setSessionId(rm.getSessionId());
						pr.add(getPipe(source), resp);
					}
				}
			}

			return result;
		}
	};

	private Node destination = new Node() {
		@Override
		protected List<ProcessResult> process(
				Map<Pipe, List<DataMessage>> events) {
			List<ProcessResult> result = new ArrayList<ProcessResult>();
			ProcessResult pr = new ProcessResult();
			result.add(pr);

			for (Entry<Pipe, List<DataMessage>> entry : events.entrySet()) {
				for (DataMessage dm : entry.getValue()) {
					ResponseMessage rm = new ResponseMessage(dm) {
					};
					rm.setSessionId(dm.getSessionId());
					pr.add(entry.getKey(), rm);
				}
			}

			return result;
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
