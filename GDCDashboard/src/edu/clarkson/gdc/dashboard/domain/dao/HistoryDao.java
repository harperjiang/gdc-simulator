package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Date;
import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;

public interface HistoryDao {

	public List<NodeHistory> getHistories(Node node, String dataType,
			Date from, Date to);

	public List<NodeHistory> getHistories(Node node, String dataType, int count);

	public NodeHistory getLatest(Node node, String dataType);
	
	public void addHistory(NodeHistory history);
	
	public void cleanHistory(Date timepoint);
}
