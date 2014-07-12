package edu.clarkson.cs.gdc.network.ripeatlas.task;

import edu.clarkson.cs.gdc.network.Service;
import edu.clarkson.cs.gdc.network.ripeatlas.api.probe.ProbeListResponse;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Probe;
import edu.clarkson.cs.gdc.network.ripeatlas.model.ProbeSpec;
import edu.clarkson.cs.gdc.network.ripeatlas.model.ProbeStatus;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteTarget;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasureWorldAnchor {

	static Logger logger = LoggerFactory.getLogger(MeasureWorldAnchor.class);

	static Service service = new Service();

	static Random random = new Random(System.currentTimeMillis());

	public static void main(String[] args) throws Exception {
		List<Probe> anchors = new ArrayList<Probe>();

		int start = 0;
		int limit = 100;
		while (true) {
			ProbeListResponse response;
			try {
				response = service.probes()
						.list(start, limit, "is_public", "true").execute();
			} catch (Exception e) {
				// Simply retry
				logger.error("Error, retry", e);
				Thread.sleep(2000);
				continue;
			}
			List<Probe> probes = response.getResult();
			for (Probe probe : probes) {
				if (probe.isAnchor()
						&& probe.getStatus() == ProbeStatus.Connected.ordinal()) {
					anchors.add(probe);
				}
			}
			if (StringUtils.isEmpty(response.getNextPageUrl())) {
				break;
			} else {
				start += probes.size();
			}
		}

		for (Probe anchor : anchors) {
			measure(anchor, 10);
		}
	}

	private static void measure(Probe anchor, int source) throws Exception {

		MeasurementCreate mc = new MeasurementCreate();

		TracerouteTarget tt = new TracerouteTarget();
		tt.setDescription(MessageFormat.format("WorldAnchorMeasure_{0}",
				anchor.getAddressV4()));
		tt.setAf(4);
		tt.setType("traceroute");
		tt.setOneoff(true);
		tt.setTarget(anchor.getAddressV4());
		tt.setProtocol("TCP");
		mc.getTargets().add(tt);

		ProbeSpec probeSpec = new ProbeSpec();
		probeSpec.setRequested(source);
		probeSpec.setType("area");
		probeSpec.setValue("WW");
		mc.getProbes().add(probeSpec);

		service.measurements().create(mc).execute();
	}
}
