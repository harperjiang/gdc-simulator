package edu.clarkson.cs.network.ripeatlas.task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.cs.network.Service;
import edu.clarkson.cs.network.ipinfo.api.RingNodeDao;
import edu.clarkson.cs.network.ripeatlas.api.probe.ProbeListResponse;
import edu.clarkson.cs.network.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.network.ripeatlas.model.Probe;
import edu.clarkson.cs.network.ripeatlas.model.ProbeSpec;
import edu.clarkson.cs.network.ripeatlas.model.ProbeStatus;
import edu.clarkson.cs.network.ripeatlas.model.TracerouteTarget;

public class MeasureUSRingNode {

	static Logger logger = LoggerFactory.getLogger(MeasureUSRingNode.class);

	static Service service = new Service();
	
	static RingNodeDao ringNodeDao = new RingNodeDao();

	static Random random = new Random(System.currentTimeMillis());

	public static void main(String[] args) throws Exception {
		Map<String, String> countryCodes = loadCountryCode();
		Map<String, List<Probe>> probeCollections = new HashMap<String, List<Probe>>();

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
				if (probe.getStatus() == ProbeStatus.Connected.ordinal()) {
					String country = probe.getCountryCode();
					if (StringUtils.isEmpty(country)) {
						country = "UNKNOWN";
					}
					String continent = countryCodes.get(country);
					if (StringUtils.isEmpty(continent)) {
						if ("UK".equals(country)) {
							continent = "EU";
						} else {
							System.out.println("Unknown Continent for Country:"
									+ country);
							continent = "UNKNOWN";
						}
					}
					if (!probeCollections.containsKey(continent)) {
						probeCollections.put(continent, new ArrayList<Probe>());
					}
					probeCollections.get(continent).add(probe);
				}
			}
			if (StringUtils.isEmpty(response.getNextPageUrl())) {
				break;
			} else {
				start += probes.size();
			}
		}

		for (Entry<String, List<Probe>> entry : probeCollections.entrySet()) {
			System.out.println(MessageFormat.format("{0}:{1}", entry.getKey(),
					entry.getValue().size()));
		}
		//
		crossMeasure(probeCollections.get("AS"), "AS", 10, 5);
		crossMeasure(probeCollections.get("EU"), "EU", 10, 5);
		crossMeasure(probeCollections.get("NA"), "NA", 10, 5);
		crossMeasure(probeCollections.get("OC"), "OC", 5, 5);
		crossMeasure(probeCollections.get("SA"), "SA", 5, 5);
		crossMeasure(probeCollections.get("AF"), "AF", 5, 5);

		worldMeasure(probeCollections.get("AS"), "AS", 5, 10);
		worldMeasure(probeCollections.get("EU"), "EU", 5, 10);
		worldMeasure(probeCollections.get("NA"), "NA", 5, 10);
		worldMeasure(probeCollections.get("OC"), "OC", 5, 10);
		worldMeasure(probeCollections.get("SA"), "SA", 5, 10);
		worldMeasure(probeCollections.get("AF"), "AF", 5, 10);
	}

	private static void worldMeasure(List<Probe> probes, String name, int size,
			int source) throws Exception {
		for (int i = 0; i < size; i++) {
			MeasurementCreate mc = new MeasurementCreate();
			Probe targetProbe = probes.get(random.nextInt(probes.size()));

			TracerouteTarget tt = new TracerouteTarget();
			tt.setDescription(MessageFormat.format("WM_{0}_{1}", name, i));
			tt.setAf(4);
			tt.setType("traceroute");
			tt.setOneoff(true);
			tt.setTarget(targetProbe.getAddressV4());
			tt.setProtocol("UDP");
			mc.getTargets().add(tt);

			ProbeSpec probeSpec = new ProbeSpec();
			probeSpec.setRequested(source);
			probeSpec.setType("area");
			probeSpec.setValue("WW");
			mc.getProbes().add(probeSpec);

			service.measurements().create(mc).execute();
		}
	}

	private static void crossMeasure(List<Probe> probes, String name,
			int source, int target) throws Exception {
		for (int i = 0; i < target; i++) {
			MeasurementCreate mc = new MeasurementCreate();
			Probe targetProbe = probes.get(random.nextInt(probes.size()));
			StringBuilder sourceProbes = new StringBuilder();
			for (int j = 0; j < source; j++) {
				Probe srcProbe = probes.get(random.nextInt(probes.size()));
				if (srcProbe == targetProbe) {
					j--;
					continue;
				} else {
					sourceProbes.append(srcProbe.getId()).append(",");
				}
			}
			sourceProbes.deleteCharAt(sourceProbes.length() - 1);

			TracerouteTarget tt = new TracerouteTarget();
			tt.setDescription(MessageFormat.format("WC_{0}_{1}", name, i));
			tt.setAf(4);
			tt.setType("traceroute");
			tt.setOneoff(true);
			tt.setTarget(targetProbe.getAddressV4());
			tt.setProtocol("UDP");
			mc.getTargets().add(tt);

			ProbeSpec probeSpec = new ProbeSpec();
			probeSpec.setRequested(source);
			probeSpec.setType("probes");
			probeSpec.setValue(sourceProbes.toString());
			mc.getProbes().add(probeSpec);

			service.measurements().create(mc).execute();
		}
	}

	private static Map<String, String> loadCountryCode() throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(
						"src/gdc/network/ripeatlas/snippet/country_code")));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] split = line.split("\\s");
			result.put(split[0], split[1]);
		}
		br.close();
		return result;
	}
}
