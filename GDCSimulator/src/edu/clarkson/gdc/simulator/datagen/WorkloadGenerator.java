package edu.clarkson.gdc.simulator.datagen;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WorkloadGenerator {

	static final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	static final DateFormat detail = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static void generate(String fileName, Date start, Date end)
			throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
		Random random = new Random(System.currentTimeMillis());
		while (start.compareTo(end) < 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(start);
			calendar.add(Calendar.SECOND, 50 + random.nextInt(1000));
			start = calendar.getTime();
			// Generate Words
			List<String> words = new ArrayList<String>();
			int wordCount = 1 + random.nextInt(7);
			for (int i = 0; i < wordCount; i++) {
				int wordLength = 1 + random.nextInt(8);
				StringBuilder wordBuffer = new StringBuilder();
				for (int j = 0; j < wordLength; j++) {
					wordBuffer.append((char) ('a' + random.nextInt(26)));
				}
				words.add(wordBuffer.toString());
			}
			StringBuilder wordsb = new StringBuilder();
			for (String word : words)
				wordsb.append(word + ";");
			pw.println(MessageFormat.format("{0},{1}", df.format(start),
					wordsb.toString()));
		}
		pw.close();
	}

	public static void main(String[] args) throws Exception {
		generate("workload_file1.txt",
				detail.parse("20110101000000000", new ParsePosition(0)),
				detail.parse("20110601000000000", new ParsePosition(0)));
		generate("workload_file2.txt",
				detail.parse("20110101000000000", new ParsePosition(0)),
				detail.parse("20110601000000000", new ParsePosition(0)));
		generate("workload_file3.txt",
				detail.parse("20110101000000000", new ParsePosition(0)),
				detail.parse("20110601000000000", new ParsePosition(0)));
	}
}
