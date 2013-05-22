package edu.clarkson.gdc.simulator.scenario.latency.simple.exstr;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.clarkson.gdc.simulator.ExceptionStrategy;
import edu.clarkson.gdc.simulator.framework.NodeException;
import edu.clarkson.gdc.simulator.framework.StrategyException;
import edu.clarkson.gdc.simulator.framework.utils.FileCursor;
import edu.clarkson.gdc.simulator.framework.utils.FileCursor.LineProcessor;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class RangeExceptionStrategy implements ExceptionStrategy {

	public static class Range {

		long start;

		long stop;

		boolean value;

		public Range(long start, long stop, boolean val) {
			this.start = start;
			this.stop = stop;
			this.value = val;
		}

		public long getStart() {
			return start;
		}

		public void setStart(long start) {
			this.start = start;
		}

		public long getStop() {
			return stop;
		}

		public void setStop(long stop) {
			this.stop = stop;
		}

		public boolean getValue() {
			return value;
		}

		public void setValue(boolean value) {
			this.value = value;
		}

	}

	private Range current;

	public RangeExceptionStrategy() {
	}

	@Override
	public NodeException getException(long tick) {
		if (null == current)
			current = fileCursor.next();
		if (null == current)
			return null;
		if (current.getStart() <= tick && current.getStop() > tick) {
			return current.value ? null : new StrategyException();
		} else {
			current = null;
			return getException(tick);
		}
	}

	private FileCursor<Range> fileCursor;

	static DateFormat format = new SimpleDateFormat("yyyyMMddHH");
	static Pattern pattern = Pattern.compile("(\\d+),(\\d)");

	public void load(String fileName, final int unit) {
		fileCursor = new FileCursor<Range>(fileName);
		fileCursor.setLineProcessor(new LineProcessor<Range>() {

			long last = 0;
			long offset = 0;
			boolean lastval = false;

			@Override
			public Range process(String line) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					String dateString = matcher.group(1);
					String value = matcher.group(2);
					Date date = format.parse(dateString, new ParsePosition(0));
					long time = date.getTime();
					Range retval = null;
					if (last == 0) {
						offset = time;
					} else {
						retval = new Range((last - offset) / unit,
								(time - offset) / unit, lastval);
					}
					lastval = value.equals("1");
					last = time;
					return retval;
				}
				return null;
			}
		});
	}
}
