package edu.clarkson.cs.gdc.network.ripeatlas.snippet;

import edu.clarkson.cs.gdc.network.ripeatlas.api.Service;
import edu.clarkson.cs.gdc.network.ripeatlas.api.common.Request;
import edu.clarkson.cs.gdc.network.ripeatlas.api.common.Response;
import edu.clarkson.cs.gdc.network.ripeatlas.api.measurement.MeasurementGetResponse;
import edu.clarkson.cs.gdc.network.ripeatlas.api.measurement.MeasurementResultResponse;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Measurement;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Output;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteOutput;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteOutput.TracerouteData;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalyzeMeasureWorld {

	static Service service = new Service();

	static long total = 0;
	static long success = 0;

	static Set<String> distinctIp = new HashSet<String>();

	public static void main(String[] args) throws Exception {

		// PrintWriter pw = new PrintWriter(new FileOutputStream(
		// "traceroute_output"));

		for (int i = 1618159; i <= 1618208; i++) {
			loadMeasurement(i);
		}
		// pw.close();
		System.out.println(MessageFormat.format("Total {0}, success {1}",
				total, success));
		System.out.println(MessageFormat.format(
				"{0} distinct ips are collected", distinctIp.size()));
	}

	protected static void loadMeasurement(int id) throws Exception {
		MeasurementGetResponse get = execute(service.measurements().get(
				String.valueOf(id)));

		Measurement measurement = get.getResult();
		if (measurement.getDescription().startsWith("WorldAnchorMeasure")) {
			MeasurementResultResponse resultResp = execute(service
					.measurements().result(String.valueOf(id)));
			List<MeasurementResult> results = resultResp.getResult();
			for (MeasurementResult result : results) {
				total++;
				// Check the last output to see whether it is the same as job
				// target
				Set<String> targets = new HashSet<String>();
				for (Output output : result.getOutputs()) {
					if (output instanceof TracerouteOutput) {
						for (TracerouteData data : ((TracerouteOutput) output)
								.getData()) {
							targets.add(data.getFrom());
						}
					}
				}
				if (targets.contains(result.getDstName())) {
					success++;
				}
				distinctIp.addAll(targets);
			}
		}
	}

	protected static <T extends Response<?>> T execute(Request<T> request)
			throws Exception {
		while (true) {
			try {
				T response = request.execute();
				if (null == response.getError()) {
					return response;
				} else {
					System.out.println(response.getError().getMessage());
				}
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.sleep(2000);
			}
		}
	}
}
