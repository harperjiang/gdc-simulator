package edu.clarkson.gdc.simsupport.datagen;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AvailabilityGenerator {

	static final DateFormat df = new SimpleDateFormat("yyyyMMddHH");

	static final DateFormat detail = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static void generate(String fileName, Date start, Date end)
			throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
		Random random = new Random(System.currentTimeMillis());
		while (start.compareTo(end) < 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(start);
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			start = calendar.getTime();
			int value = (random.nextFloat() >= 0.5f) ? 1 : 0;
			pw.println(MessageFormat.format("{0},{1}", df.format(start), value));
		}
		pw.close();
	}

	public static void main(String[] args) throws Exception {
		generate("data_site3.txt",
				detail.parse("20110101000000000", new ParsePosition(0)),
				detail.parse("20120101000000000", new ParsePosition(0)));
		generate("data_site4.txt",
				detail.parse("20110101000000000", new ParsePosition(0)),
				detail.parse("20120101000000000", new ParsePosition(0)));
		generate("data_site5.txt",
				detail.parse("20110101000000000", new ParsePosition(0)),
				detail.parse("20120101000000000", new ParsePosition(0)));
		
	}
}
