package edu.clarkson.cs.network.ripeatlas.task;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import edu.clarkson.cs.network.Service;
import edu.clarkson.cs.network.common.Request;
import edu.clarkson.cs.network.common.Response;
import edu.clarkson.cs.network.ripeatlas.api.measurement.MeasurementGetResponse;
import edu.clarkson.cs.network.ripeatlas.api.measurement.MeasurementResultResponse;
import edu.clarkson.cs.network.ripeatlas.model.Measurement;
import edu.clarkson.cs.network.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.network.ripeatlas.model.Output;
import edu.clarkson.cs.network.ripeatlas.model.TracerouteOutput;
import edu.clarkson.cs.network.ripeatlas.model.TracerouteOutput.TracerouteData;

public class AnalyzeMeasureWorld {

	public static class Section {

		String sourceIp;

		String fromIp;

		String toIp;

		BigDecimal rtt;

		BigDecimal rttFromSource;
	}

	static Service service = new Service();

	public static void main(String[] args) throws Exception {

		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"traceroute_output"));

		for (int i = 1617885; i <= 1617978; i++) {
			loadMeasurement(i, pw);
		}
		pw.close();
	}

	protected static void loadMeasurement(int id, PrintWriter pw)
			throws Exception {
		MeasurementGetResponse get = execute(service.measurements().get(
				String.valueOf(id)));

		Measurement measurement = get.getResult();
		if (measurement.getDescription().startsWith("MeasureWorld")) {
			MeasurementResultResponse resultResp = execute(service
					.measurements().result(String.valueOf(id)));
			List<MeasurementResult> results = resultResp.getResult();
			for (MeasurementResult result : results) {

				List<Section> sections = new ArrayList<Section>();

				for (Output output : result.getOutputs()) {
					if (validStep(output)) {
						TracerouteOutput to = (TracerouteOutput) output;
						TracerouteData summary = summarize(to.getData());
						Section section = new Section();
						section.toIp = summary.getFrom();

						Section validStart = validStart(sections, summary);

						if (validStart == null) {
							section.fromIp = result.getFrom();
							section.rtt = summary.getRoundTripTime();
						} else {
							section.fromIp = validStart.toIp;
							section.rtt = summary.getRoundTripTime().subtract(
									validStart.rttFromSource);
						}
						section.rttFromSource = summary.getRoundTripTime();
						section.sourceIp = result.getFrom();
						sections.add(section);
					}
				}
				// Output section
				for (Section s : sections) {
					pw.println(MessageFormat
							.format("insert into trace_data (source_ip,from_ip,to_ip,rtt,rtt_source) values(''{0}'',''{1}'',''{2}'',{3,number,0.0000},{4,number,0.0000});",
									s.sourceIp, s.fromIp, s.toIp, s.rtt,
									s.rttFromSource));
				}
			}
		}
	}

	private static Section validStart(List<Section> exist, TracerouteData data) {
		for (int i = exist.size() - 1; i > 0; i--) {
			if (exist.get(i).rttFromSource.compareTo(data.getRoundTripTime()) <= 0)
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
