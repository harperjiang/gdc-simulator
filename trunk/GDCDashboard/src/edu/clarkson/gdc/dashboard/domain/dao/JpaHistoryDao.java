package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Date;
import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;

public class JpaHistoryDao extends JpaDaoSupport implements HistoryDao {

	@Override
	public List<NodeHistory> getHistories(Node node, String dataType,
			Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeHistory getLatest(Node node, String dataType) {
		// TODO Auto-generated method stub
		return null;
	}

}
