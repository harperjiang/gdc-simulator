package edu.clarkson.gdc.simulator.scenario.latency.solar2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class SunriseProcessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("/home/harper/sunrise")));
		PrintWriter pw = new PrintWriter(new FileOutputStream("/home/harper/sunrise_time"));

		String line = null;
		while ((line = br.readLine()) != null) {
			String[] data = line.split("[\t ]+");
			pw.print(MessageFormat.format("{0}\t{1}\n",
					process(Integer.valueOf(data[0])),
					process(Integer.valueOf(data[1]))));
		}
		br.close();
		pw.close();
	}

	protected static double process(int val) {
		int big = val / 100;
		double small = val - big * 100;
		return big + small / 60;
	}
}
