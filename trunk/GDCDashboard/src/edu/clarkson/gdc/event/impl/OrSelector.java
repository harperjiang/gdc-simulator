package edu.clarkson.gdc.event.impl;

import java.util.List;

import edu.clarkson.gdc.event.Event;
import edu.clarkson.gdc.event.EventSelector;

public class OrSelector implements EventSelector {

	private List<EventSelector> selectors;

	public List<EventSelector> getSelectors() {
		return selectors;
	}

	public void setSelectors(List<EventSelector> selectors) {
		this.selectors = selectors;
	}

	@Override
	public boolean accept(Event event) {
		for (EventSelector selector : selectors)
			if (selector.accept(event))
				return true;
		return false;
	}

}
