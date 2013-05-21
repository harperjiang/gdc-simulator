package edu.clarkson.gdc.simulator.scenario.latency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.session.Session;
import edu.clarkson.gdc.simulator.framework.session.SessionManager;
import edu.clarkson.gdc.simulator.scenario.AbstractDataCenter;
import edu.clarkson.gdc.simulator.scenario.latency.twopc.FinalizeMessage;
import edu.clarkson.gdc.simulator.scenario.latency.twopc.FinalizeResponse;
import edu.clarkson.gdc.simulator.scenario.latency.twopc.VoteMessage;
import edu.clarkson.gdc.simulator.scenario.latency.twopc.VoteResponse;

public class TwoPCServer extends AbstractDataCenter {

	protected List<Pipe> serverPipes;

	protected SessionManager sessionManager;

	protected static String TRANSACTION = "transaction";

	protected static String MESSAGE = "message";

	protected static String PIPE = "pipe";

	public TwoPCServer() {
		super();
		serverPipes = new ArrayList<Pipe>();
		sessionManager = new SessionManager();
	}

	protected void init() {
		for (Entry<Node, Pipe> entry : pipes.entrySet()) {
			if (entry.getKey() instanceof TwoPCServer) {
				serverPipes.add(entry.getValue());
			}
		}
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof ClientRead) {
			recorder.record(30l, 100l, source,
					new ClientResponse(message, true));
		}

		if (message instanceof ClientWrite) {
			ClientWrite cw = (ClientWrite) message;
			// Start 2-pc commit, send voting message
			for (Pipe pipe : serverPipes) {
				// Need some time to prepare 2-pc env
				VoteMessage vm = new VoteMessage(message.getSessionId(),
						cw.getData());
				recorder.record(60l, 0l, pipe, vm);
			}
			Session session = sessionManager.createSession(message
					.getSessionId());
			session.put(TRANSACTION, new TransactionStatus(serverPipes.size()));
			session.put(MESSAGE, message);
			session.put(PIPE, source);
		}
		if (message instanceof VoteMessage) {
			// TODO Always accept?
			VoteMessage vote = (VoteMessage) message;
			// This time is the same as IsolatedServer
			recorder.record(40l, 120l, source, new VoteResponse(vote, true));
		}
		if (message instanceof VoteResponse) {
			VoteResponse vr = (VoteResponse) message;
			TransactionStatus tran = sessionManager.getSession(
					message.getSessionId()).get(TRANSACTION);
			if (tran != null) {
				tran.receiveResponse(vr.isAccept());
				if (tran.canFinalize()) {
					for (Pipe pipe : serverPipes) {
						recorder.record(30l, 0l, pipe, new FinalizeMessage(
								message.getSessionId(), tran.canCommit()));
					}
				}
			}
		}
		if (message instanceof FinalizeMessage) {
			FinalizeMessage fm = (FinalizeMessage) message;
			if (fm.isCommit()) {
				recorder.record(30l, 0l, source, new FinalizeResponse(
						(FinalizeMessage) message));
			} else {
				// Rollback takes more time than commit
				recorder.record(60l, 120l, source, new FinalizeResponse(
						(FinalizeMessage) message));
			}
		}
		if (message instanceof FinalizeResponse) {
			Session session = sessionManager.getSession(message.getSessionId());
			TransactionStatus tran = session.get(TRANSACTION);
			if (null != tran) {
				tran.receiveAck();
				if (tran.isFinalized()) {
					ClientWrite original = session.get(MESSAGE);
					Pipe pipe = session.get(PIPE);
					// Failed Transaction need to rollback, which requires a
					// write time
					recorder.record(30l, 0l, pipe, new ClientResponse(original,
							tran.isSuccess()));
					sessionManager.discardSession(session);
				}
			}
		}
	}

	protected class TransactionStatus {

		private int voteCounter;

		private int ackCounter;

		private boolean accept = true;

		public TransactionStatus(int count) {
			this.voteCounter = count;
			this.ackCounter = count;
		}

		public void receiveResponse(boolean newa) {
			voteCounter--;
			accept = accept && newa;
		}

		public void receiveAck() {
			ackCounter--;
		}

		public boolean canFinalize() {
			return voteCounter == 0;
		}

		public boolean canCommit() {
			return voteCounter == 0 && accept;
		}

		public boolean isFinalized() {
			return ackCounter == 0;
		}

		public boolean isSuccess() {
			return ackCounter == 0 && accept;
		}
	}
}
