package edu.clarkson.gdc.event.impl;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.event.Event;
import edu.clarkson.gdc.event.EventParser;
import edu.clarkson.gdc.event.EventSender;
import edu.clarkson.gdc.event.EventSubscriber;

public class EventMediator implements EventSender {

	private List<EventParser> parsers;

	private List<EventSubscriber> subscribers;

	public EventMediator() {
		subscribers = new ArrayList<EventSubscriber>();
	}

	public List<EventParser> getParsers() {
		return parsers;
	}

	public void setParsers(List<EventParser> parsers) {
		this.parsers = parsers;
	}

	public void addSubscriber(EventSubscriber sub) {
		subscribers.add(sub);
	}

	@Override
	public void send(Object eventsrc) {
		if (null == eventsrc)
			return;
		for (EventParser parser : parsers) {
			Event event = parser.parse(eventsrc);
			if (null != event) {
				for (EventSubscriber subscriber : subscribers) {
					if (subscriber.getSelector().accept(event))
						subscriber.onEvent(event);
				}
			}
		}
	}

}
