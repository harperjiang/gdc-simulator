package edu.clarkson.gdc.simulator.scenario.tact;

import java.util.ArrayList;
import java.util.List;

public class SortingList<T extends Comparable<T>> {

	private List<T> content;

	public SortingList() {
		super();
		content = new ArrayList<T>();
	}

	public List<T> getList() {
		return content;
	}

	/*
	 * As most of the add function occurred on the bottom of the list, a linear
	 * search may be faster than a binary search
	 */
	public void add(T object) {
		int index = position_binary(object);
		if (index == content.size())
			content.add(object);
		else
			content.add(index, object);
	}

	public int size() {
		return content.size();
	}

	public List<T> removebefore(T min) {
		List<T> newc = new ArrayList<T>();
		// int position = position(min);
		// List<T> newcontent = content.subList(position, content.size());
		// List<T> toremove = content.subList(0, position);
		// content = new ArrayList<T>();
		// content.addAll(newcontent);
		// return toremove;
		while (content.get(0).compareTo(min) < 0) {
			newc.add(content.remove(0));
		}
		return newc;
	}

	protected int position_postlinear(T val) {
		if (content.size() == 0)
			return 0;
		if (content.get(content.size() - 1).compareTo(val) <= 0)
			return content.size();
		for (int pos = content.size() - 1; pos > 1; pos--) {
			T elem = content.get(pos);
			T prev = content.get(pos - 1);
			if (elem.compareTo(val) >= 0 && prev.compareTo(val) <= 0)
				return pos;
		}
		int compare0 = content.get(0).compareTo(val);
		if (compare0 > 0) {
			return 0;
		}
		return 1;
	}

	protected int position_binary(T val) {
		if (content.size() == 0)
			return 0;
		int compare0 = content.get(0).compareTo(val);
		if (compare0 > 0) {
			return 0;
		}
		if (content.get(content.size() - 1).compareTo(val) <= 0)
			return content.size();
		return position(0, size() - 1, val);
	}

	protected int position(int start, int end, T val) {
		if (start == end) {
			return start + 1;
		}
		if (start == end - 1) {
			return end;
		}
		int middle = (start + end) / 2;

		int compare = content.get(middle).compareTo(val);
		if (compare == 0)
			return middle + 1;
		return compare > 0 ? position(start, middle, val) : position(middle,
				end, val);
	}
}
