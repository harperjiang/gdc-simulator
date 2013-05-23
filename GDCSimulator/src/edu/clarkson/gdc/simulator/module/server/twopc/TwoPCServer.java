package edu.clarkson.gdc.simulator.module.server.twopc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.session.Session;
import edu.clarkson.gdc.simulator.framework.session.SessionManager;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;
import edu.clarkson.gdc.simulator.module.server.AbstractDataCenter;

public class TwoPCServer extends AbstractDataCenter {

	public static final String READ_DATA = "read_data";

	public static final String WRITE_DATA = "write_data";

	public static final String SEND_VOTE = "send_vote";

	public static final String RECEIVE_VOTE = "receive_vote";

	public static final String SEND_FINALIZE = "send_finalize";

	public static final String RECEIVE_FINALIZE = "receive_finalize";

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
		if (message instanceof KeyRead) {
			KeyRead cr = (KeyRead) message;
			Pair<Long, Data> readresult = getStorage().read(cr.getKey());
			recorder.record(getCpuCost(READ_DATA), readresult.getA(), source,
					new KeyResponse(message, readresult.getB()));
		}

		if (message instanceof KeyWrite) {
			KeyWrite cw = (KeyWrite) message;
			// Start 2-pc commit, send voting message
			for (Pipe pipe : serverPipes) {
				// Need some time to prepare 2-pc env
				VoteMessage vm = new VoteMessage(message.getSessionId(),
						cw.getData());
				recorder.record(getCpuCost(SEND_VOTE), 0l, pipe, vm);
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
			recorder.record(getCpuCost(RECEIVE_VOTE), getStorage()
					.getWriteTime(), source, new VoteResponse(vote, true));
		}
		if (message instanceof VoteResponse) {
			VoteResponse vr = (VoteResponse) message;
			TransactionStatus tran = sessionManager.getSession(
					message.getSessionId()).get(TRANSACTION);
			if (tran != null) {
				tran.receiveResponse(vr.isAccept());
				if (tran.canFinalize()) {
					for (Pipe pipe : serverPipes) {
						recorder.record(getCpuCost(SEND_FINALIZE), 0l, pipe,
								new FinalizeMessage(message.getSessionId(),
										tran.canCommit()));
					}
				}
			}
		}
		if (message instanceof FinalizeMessage) {
			FinalizeMessage fm = (FinalizeMessage) message;
			if (fm.isCommit()) {
				recorder.record(getCpuCost(RECEIVE_FINALIZE), 0l, source,
						new FinalizeResponse((FinalizeMessage) message));
			} else {
				// Rollback takes more time than commit
				recorder.record(getCpuCost(RECEIVE_FINALIZE), getStorage()
						.getWriteTime(), source, new FinalizeResponse(
						(FinalizeMessage) message));
			}
		}
		if (message instanceof FinalizeResponse) {
			Session session = sessionManager.getSession(message.getSessionId());
			TransactionStatus tran = session.get(TRANSACTION);
			if (null != tran) {
				tran.receiveAck();
				if (tran.isFinalized()) {
					KeyWrite original = session.get(MESSAGE);
					Pipe pipe = session.get(PIPE);
					// Failed Transaction need to rollback, which requires a
					// write time
					recorder.record(getCpuCost(WRITE_DATA), 0l, pipe,
							new KeyResponse(original, tran.isSuccess()));
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
			return voteCounter == 0 || !accept;
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
