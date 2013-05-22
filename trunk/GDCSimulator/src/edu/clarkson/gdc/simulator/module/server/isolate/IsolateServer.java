package edu.clarkson.gdc.simulator.module.server.isolate;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.module.message.ClientRead;
import edu.clarkson.gdc.simulator.module.message.ClientResponse;
import edu.clarkson.gdc.simulator.module.message.ClientWrite;
import edu.clarkson.gdc.simulator.module.server.AbstractDataCenter;

public class IsolateServer extends AbstractDataCenter {

	public static final String READ_DATA = "read_data";

	public static final String WRITE_DATA = "write_data";

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof ClientRead) {
			ClientRead cr = (ClientRead) message;
			Pair<Long, Data> readresult = getStorage().read(cr.getKey());
			recorder.record(getCpuCost(READ_DATA), readresult.getA(), source,
					new ClientResponse(message, readresult.getB()));
		}

		if (message instanceof ClientWrite) {
			ClientWrite cw = (ClientWrite) message;
			long time = getStorage().write(cw.getData());
			recorder.record(getCpuCost(WRITE_DATA), time, source,
					new ClientResponse(message));
		}
	}
}
