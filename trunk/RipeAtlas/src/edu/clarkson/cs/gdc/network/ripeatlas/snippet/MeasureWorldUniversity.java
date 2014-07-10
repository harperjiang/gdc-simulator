package edu.clarkson.cs.gdc.network.ripeatlas.snippet;

import edu.clarkson.cs.gdc.network.Service;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.gdc.network.ripeatlas.model.ProbeSpec;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteTarget;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasureWorldUniversity {

	static Logger logger = LoggerFactory
			.getLogger(MeasureWorldUniversity.class);

	static Service service = new Service();

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(
						"src/gdc/network/ripeatlas/snippet/target")));
		String line = null;
		while ((line = br.readLine()) != null) {
			measureTarget(line.trim(), 10);
		}

		br.close();
	}

	private static void measureTarget(String target, int count)
			throws Exception {

		MeasurementCreate mc = new MeasurementCreate();

		TracerouteTarget tt = new TracerouteTarget();
		tt.setDescription(MessageFormat.format("MeasureWorld_ICMP_{0}", target));
		tt.setAf(4);
		tt.setType("traceroute");
		tt.setOneoff(true);
		tt.setTarget(target);
		tt.setProtocol("ICMP");
		
		mc.getTargets().add(tt);

		ProbeSpec probeSpec = new ProbeSpec();
		probeSpec.setRequested(count);
		probeSpec.setType("area");
		probeSpec.setValue("WW");
		mc.getProbes().add(probeSpec);

		service.measurements().create(mc).execute();
	}

}
