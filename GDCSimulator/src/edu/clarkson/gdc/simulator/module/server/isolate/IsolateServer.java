package edu.clarkson.gdc.simulator.module.server.isolate;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.storage.NotFoundException;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;
import edu.clarkson.gdc.simulator.module.server.AbstractDataCenter;

public class IsolateServer extends AbstractDataCenter {

	public static final String READ_DATA = "read_data";

	public static final String WRITE_DATA = "write_data";

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof KeyRead) {
			KeyRead cr = (KeyRead) message;
			try {
				Pair<Long, Data> readresult = getStorage().read(cr.getKey());
				recorder.record(getCpuCost(READ_DATA), readresult.getA(),
						source, new KeyResponse(message, readresult.getB()));
			} catch (NotFoundException e) {
				recorder.record(getCpuCost(READ_DATA), getStorage()
						.getReadTime(), source, new KeyResponse(message, null));
			}
		}

		if (message instanceof KeyWrite) {
			KeyWrite cw = (KeyWrite) message;
			long time = getStorage().write(cw.getData());
			recorder.record(getCpuCost(WRITE_DATA), time, source,
					new KeyResponse(message));
		}
	}
}
