package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Date;
import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.EventLog;
import edu.clarkson.gdc.dashboard.domain.entity.Node;

public interface EventLogDao {

	public List<EventLog> getEventLog(Node node, String dataType, Date from,
			Date to);

	public List<EventLog> getEventLog(Node node, String dataType, int limit);
}
