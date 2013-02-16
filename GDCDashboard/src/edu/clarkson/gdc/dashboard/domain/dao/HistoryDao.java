package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Date;
import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;

public interface HistoryDao {

	public List<NodeHistory> getHistories(Node node, String dataType,
			Date from, Date to);

	public NodeHistory getLatest(Node node, String dataType);
}
