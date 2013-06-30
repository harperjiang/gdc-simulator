package edu.clarkson.gdc.dashboard.service.ai;

import java.text.MessageFormat;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.event.Event;
import edu.clarkson.gdc.event.EventParser;
import edu.clarkson.gdc.event.impl.DefaultEvent;

public class AlertParser implements EventParser {

	@Override
	public Event parse(Object input) {
		if (input instanceof Alert) {
			DefaultEvent event = new DefaultEvent();
			Alert alert = (Alert) input;
			event.setContent(input);
			event.setType(MessageFormat.format("{0}-{1}",
					Alert.class.getSimpleName(), alert.getType().name()));
			return event;
		}
		return null;
	}
}
