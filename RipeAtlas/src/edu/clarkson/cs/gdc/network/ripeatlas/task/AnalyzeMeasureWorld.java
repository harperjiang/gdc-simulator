package edu.clarkson.cs.gdc.network.ripeatlas.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import edu.clarkson.cs.gdc.network.Service;
import edu.clarkson.cs.gdc.network.common.Request;
import edu.clarkson.cs.gdc.network.common.Response;
import edu.clarkson.cs.gdc.network.ripeatlas.api.measurement.MeasurementGetResponse;
import edu.clarkson.cs.gdc.network.ripeatlas.api.measurement.MeasurementResultResponse;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Measurement;
import edu.clarkson.cs.gdc.network.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.gdc.network.ripeatlas.model.Output;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteOutput;
import edu.clarkson.cs.gdc.network.ripeatlas.model.TracerouteOutput.TracerouteData;
import edu.clarkson.cs.gdc.network.tracedata.api.TraceDataDao;
import edu.clarkson.cs.gdc.network.tracedata.model.TraceData;

public class AnalyzeMeasureWorld {

	static Service service = new Service();

	static TraceDataDao traceDataDao = new TraceDataDao();

	public static void main(String[] args) throws Exception {
		for (int i = 1696261; i <= 1696294; i++) {
			loadMeasurement(i);
		}
	}

	protected static void loadMeasurement(int id) throws Exception {
		MeasurementGetResponse get = execute(service.measurements().get(
				String.valueOf(id)));

		Measurement measurement = get.getResult();
		if (measurement.getDescription().startsWith("MeasureRingNode")) {
			MeasurementResultResponse resultResp = execute(service
					.measurements().result(String.valueOf(id)));
			List<MeasurementResult> results = resultResp.getResult();
			for (MeasurementResult result : results) {

				List<TraceData> sections = new ArrayList<TraceData>();

				for (Output output : result.getOutputs()) {
					if (validStep(output)) {
						TracerouteOutput to = (TracerouteOutput) output;
						TracerouteData summary = summarize(to.getData());
						TraceData section = new TraceData();
						section.setToIp(summary.getFrom());

						TraceData validStart = validStart(sections, summary);

						if (validStart == null) {
							section.setFromIp(result.getFrom());
							section.setRtt(summary.getRoundTripTime());
						} else {
							section.setFromIp(validStart.getToIp());
							section.setRtt(summary.getRoundTripTime().subtract(
									validStart.getRttSource()));
						}
						section.setRttSource(summary.getRoundTripTime());
						section.setSourceIp(result.getFrom());
						sections.add(section);
					}
				}
				// Output section
				for (TraceData td : sections) {
					traceDataDao.save(td);
				}
			}
		}
	}

	private static TraceData validStart(List<TraceData> exist,
			TracerouteData data) {
		for (int i = exist.size() - 1; i > 0; i--) {
			if (exist.get(i).getRttSource().compareTo(data.getRoundTripTime()) <= 0)
				return exist.get(i);
		}
		return null;
	}

	private static boolean validStep(Output output) {
		if (!(output instanceof TracerouteOutput))
			return false;
		TracerouteOutput to = (TracerouteOutput) output;
		if (to.getData().size() == 0)
			return false;
		TracerouteData data = to.getData().get(0);
		if (isPrivateIp(data.getFrom()))
			return false;
		return true;
	}

	private static boolean isPrivateIp(String from) {
		if (from.startsWith("192.168."))
			return true;
		if (from.startsWith("10."))
			return true;
		if (from.startsWith("172.")) {
			String[] parts = from.split("\\.");
			Integer part2 = Integer.parseInt(parts[1]);
			if (part2 >= 16 && part2 <= 31)
				return true;
		}
		return false;
	}

	private static TracerouteData summarize(List<TracerouteData> datas) {
		TracerouteData data = new TracerouteData();
		data.setRoundTripTime(BigDecimal.ZERO);
		data.setFrom(datas.get(0).getFrom());

		BigDecimal median = null;

		if (datas.size() == 1) {
			median = datas.get(0).getRoundTripTime();
		} else {
			PriorityQueue<BigDecimal> pq = new PriorityQueue<BigDecimal>();
			for (TracerouteData d : datas) {
				pq.add(d.getRoundTripTime());
			}
			int index = (datas.size() - 1) / 2;
			for (int i = 0; i < index; i++) {
				pq.poll();
			}
			median = pq.poll();
		}
		data.setRoundTripTime(median);
		return data;
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
