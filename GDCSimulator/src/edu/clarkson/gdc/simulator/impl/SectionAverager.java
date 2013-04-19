package edu.clarkson.gdc.simulator.impl;

import java.util.ArrayList;
import java.util.List;

public class SectionAverager {

	private long sectionLength;

	private List<Section> sections;
	private long sum;
	private long count;
	private long start;

	public SectionAverager(long sl) {
		sections = new ArrayList<Section>();
		reset();
		start = 0;
		sectionLength = sl;
	}

	public void reset() {
		count = 0;
		sum = 0;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void add(long time, long val) {
		while (time > start + sectionLength) {
			Section sec = new Section();
			sec.start = start;
			sec.count = count;
			sec.average = getAverage();
			sections.add(sec);

			start += sectionLength;
			reset();
		}
		count++;
		sum += val;
	}

	public void end(long time) {
		while (time > start + sectionLength) {
			Section sec = new Section();
			sec.start = start;
			sec.count = count;
			sec.average = getAverage();
			sections.add(sec);
			start += sectionLength;
			reset();
		}
	}

	public long getAverage() {
		if (0 == count)
			return 0;
		return sum / count;
	}

	public long getCount() {
		return count;
	}

	public static final class Section {
		public long start;
		public long count;
		public long average;
	}
}
