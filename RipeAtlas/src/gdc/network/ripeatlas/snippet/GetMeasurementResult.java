package gdc.network.ripeatlas.snippet;

import gdc.network.ripeatlas.api.Service;
import gdc.network.ripeatlas.model.MeasurementResult;
import gdc.network.ripeatlas.model.Output;
import gdc.network.ripeatlas.model.TracerouteOutput;
import gdc.network.ripeatlas.model.TracerouteOutput.TracerouteData;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

public class GetMeasurementResult {

	public static void main(String[] args) throws Exception {

		Service service = new Service();

		List<MeasurementResult> results = service.measurements()
				.result("1613913").execute().getResult();
		long start = 0;
		for (MeasurementResult result : results) {
			Output o = result.getOutputs().get(result.getOutputs().size() - 1);
			if (o instanceof TracerouteOutput) {
				TracerouteOutput output = (TracerouteOutput) o;
				if (output.getData().size() > 0) {
					BigDecimal value = BigDecimal.ZERO;

					for (TracerouteData data : output.getData()) {
						value = value.add(new BigDecimal(data
								.getRoundTripTime() / 2));
					}
					value = value.divide(
							new BigDecimal(output.getData().size()), 5,
							BigDecimal.ROUND_HALF_UP);
					long time = result.getTimestamp().getTime();
					if (start == 0) {
						start = time;
						time = 0;
					} else {
						time = time - start;
					}
					System.out.println(MessageFormat.format(
							"{0}\t{1,number,.00000}", String.valueOf(time/1000),
							value));
				}
			}
		}
	}
}
