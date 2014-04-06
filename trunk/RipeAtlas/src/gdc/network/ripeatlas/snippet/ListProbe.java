package gdc.network.ripeatlas.snippet;

import gdc.network.ripeatlas.api.Service;
import gdc.network.ripeatlas.api.probe.ProbeListResponse;
import gdc.network.ripeatlas.model.Probe;
import gdc.network.ripeatlas.model.ProbeStatus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListProbe {

	static Logger logger = LoggerFactory.getLogger(ListProbe.class);

	public static void main(String[] args) throws Exception {
		Map<String, String> countryCodes = loadCountryCode();
		Map<String, List<Probe>> probeCollections = new HashMap<String, List<Probe>>();

		Service service = new Service();
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
