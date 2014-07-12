package edu.clarkson.cs.gdc.network.ipquery.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CapChecker {

	private List<Cap> caps;

	public CapChecker() {
		caps = new ArrayList<Cap>();
	}

	public void addDailyCap(int cap) {
		caps.add(new DailyCap(cap));
	}

	public void addMinuteCap(int cap) {
		caps.add(new MinuteCap(cap));
	}

	public void requested() {
		for (Cap cap : caps) {
			cap.increase();
		}
	}

	/**
	 * 
	 * @return milliseconds after which the request could be done
	 */
	public long available() {
		long value = 0;
		for (Cap cap : caps) {
			long avail = cap.available();
			if (avail > value)
				value = avail;
		}
		return value;
	}

	public boolean couldRequest() {
		for (Cap cap : caps) {
			if (cap.full())
				return false;
		}
		return true;
	}

	protected static abstract class Cap {

		private int limit;

		protected int counter;

		protected long counterTimeline;

		public Cap(int limit) {
			super();
			this.limit = limit;
			this.counter = 0;
			this.counterTimeline = System.currentTimeMillis();
		}

		public boolean full() {
			refresh();
			return counter >= limit - 1;
		}

		public void increase() {
			refresh();
			counter++;
			counterTimeline = System.currentTimeMillis();
		}

		public long available() {
			refresh();
			if (counter < limit - 1)
				return 0;
			return nextslot();
		}

		protected abstract long nextslot();

		protected abstract void refresh();
	}

	protected static class DailyCap extends Cap {

		public DailyCap(int limit) {
			super(limit);
		}

		protected void refresh() {
			Date current = new Date();

			Calendar cal = Calendar.getInstance();
			cal.setTime(current);
			int currentDay = cal.get(Calendar.DAY_OF_YEAR);
			cal.setTimeInMillis(counterTimeline);
			int lastDay = cal.get(Calendar.DAY_OF_YEAR);
			if (currentDay != lastDay) {
				counter = 0;
				counterTimeline = current.getTime();
			}
		}

		protected long nextslot() {
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DAY_OF_YEAR, 1);

			return cal.getTimeInMillis() - today.getTime();
		}
	}

	protected static class MinuteCap extends Cap {
		public MinuteCap(int limit) {
			super(limit);
		}

		protected void refresh() {
			Date current = new Date();

			Calendar cal = Calendar.getInstance();
			cal.setTime(current);
			int currentHour = cal.get(Calendar.HOUR_OF_DAY);
			int currentMinute = cal.get(Calendar.MINUTE);
			cal.setTimeInMillis(counterTimeline);
			int lastHour = cal.get(Calendar.HOUR_OF_DAY);
			int lastMinute = cal.get(Calendar.MINUTE);
			if (currentHour != lastHour || currentMinute != lastMinute) {
				counter = 0;
				counterTimeline = current.getTime();
			}
		}

		protected long nextslot() {
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.MINUTE, 1);

			return cal.getTimeInMillis() - today.getTime();
		}
	}
}
