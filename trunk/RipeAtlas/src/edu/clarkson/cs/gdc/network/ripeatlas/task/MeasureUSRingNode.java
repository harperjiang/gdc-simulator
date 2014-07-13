package edu.clarkson.cs.gdc.network.ripeatlas.task;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.cs.gdc.network.Service;
import edu.clarkson.cs.gdc.network.ipinfo.api.RingNodeDao;
import edu.clarkson.cs.gdc.network.ipinfo.model.RingNode;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.gdc.network.ripeatlas.model.ProbeSpec;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteTarget;

public class MeasureUSRingNode {

	static Logger logger = LoggerFactory.getLogger(MeasureUSRingNode.class);

	static Service service = new Service();

	static RingNodeDao ringNodeDao = new RingNodeDao();

	static Random random = new Random(System.currentTimeMillis());

	public static void main(String[] args) throws Exception {
		List<RingNode> usRings = ringNodeDao.incountry("United States");
		for (RingNode usRing : usRings) {
			measureRing(usRing, 20);
		}
	}

	private static void measureRing(RingNode ringNode, int size)
			throws Exception {
		MeasurementCreate mc = new MeasurementCreate();

		TracerouteTarget tt = new TracerouteTarget();
		tt.setDescription(MessageFormat.format("MeasureRingNode_{0}",
				ringNode.getName()));
		tt.setAf(4);
		tt.setType("traceroute");
		tt.setOneoff(true);
		tt.setTarget(ringNode.getIp());
		tt.setProtocol("TCP");
		mc.getTargets().add(tt);

		ProbeSpec probeSpec = new ProbeSpec();
		probeSpec.setRequested(size);
		probeSpec.setType("country");
		probeSpec.setValue("US");
		mc.getProbes().add(probeSpec);

		service.measurements().create(mc).execute();
	}

}
