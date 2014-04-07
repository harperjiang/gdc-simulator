package gdc.network.ripeatlas.snippet;

import gdc.network.ripeatlas.api.Service;
import gdc.network.ripeatlas.api.common.Request;
import gdc.network.ripeatlas.api.common.Response;
import gdc.network.ripeatlas.api.measurement.MeasurementGetResponse;
import gdc.network.ripeatlas.api.measurement.MeasurementResultResponse;
import gdc.network.ripeatlas.model.Measurement;
import gdc.network.ripeatlas.model.MeasurementResult;
import gdc.network.ripeatlas.model.Output;
import gdc.network.ripeatlas.model.TracerouteOutput;
import gdc.network.ripeatlas.model.TracerouteOutput.TracerouteData;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.List;

public class AnalyzeMeasurement {

	static Service service = new Service();

	public static void main(String[] args) throws Exception {

		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"traceroute_output"));
		for (int i = 1613913; i <= 1613972; i++) {
			loadMeasurement(i, pw);
		}
		pw.close();
	}

	protected static void loadMeasurement(int id, PrintWriter pw)
			throws Exception {
		MeasurementGetResponse get = execute(service.measurements().get(
				String.valueOf(id)));

		Measurement measurement = get.getResult();
		if (measurement.getDescription().startsWith("WC")
				|| measurement.getDescription().startsWith("WM")) {
			MeasurementResultResponse resultResp = execute(service
					.measurements().result(String.valueOf(id)));
			List<MeasurementResult> results = resultResp.getResult();
			for (MeasurementResult result : results) {
				for (Output output : result.getOutputs()) {
					if (output instanceof TracerouteOutput) {
						TracerouteOutput to = (TracerouteOutput) output;
						for (TracerouteData td : to.getData()) {
							pw.println(MessageFormat.format(
									"{0},{1},{2},{3},{4},{5,number,.0000}",
									String.valueOf(result.getMeasurementId()),
									result.getDstName(),result.getFrom(), to.getHop(),
									td.getFrom(), td.getRoundTripTime()));
						}
					}
				}

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
