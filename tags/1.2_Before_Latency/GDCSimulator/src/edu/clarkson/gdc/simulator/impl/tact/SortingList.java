package edu.clarkson.gdc.simulator.impl.tact;

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

	public void add(T object) {
		// TODO Change to binary insert

		if (content.isEmpty()) {
			content.add(object);
			return;
		}
		if (object.compareTo(content.get(0)) < 0) {
			content.add(0, object);
			return;
		}

		for (int i = 0; i < content.size() - 1; i++) {
			if (content.get(i).compareTo(object) <= 0
					&& content.get(i + 1).compareTo(object) > 0) {
				content.add(i + 1, object);
				return;
			}
		}
		content.add(object);
	}
}
