package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;

public class JpaHistoryDao extends JpaDaoSupport implements HistoryDao {

	@Override
	public List<NodeHistory> getHistories(Node node, String dataType,
			Date from, Date to) {
		return getJpaTemplate()
				.getEntityManager()
				.createQuery(
						"select h from NodeHistory h where h.nodeId = :nodeId "
								+ "and h.dataType = :dataType "
								+ "and h.time between :start and :end order by h.time desc",
						NodeHistory.class).setParameter("nodeId", node.getId())
				.setParameter("dataType", dataType).setParameter("start", from)
				.setParameter("end", to).getResultList();
	}

	@Override
	public NodeHistory getLatest(Node node, String dataType) {
		List<NodeHistory> result = getHistories(node, dataType, 1);
		if (result.size() == 0)
			return null;
		return result.get(0);
	}

	@Override
	public List<NodeHistory> getHistories(Node node, String dataType, int count) {
		String querystr = "select h from NodeHistory h where h.nodeId = :nodeId ";
		if (dataType != null) {
			querystr += "and h.dataType = :dataType ";
		}
		querystr += "order by h.time desc";
		TypedQuery<NodeHistory> query = getJpaTemplate().getEntityManager()
				.createQuery(querystr, NodeHistory.class)
				.setParameter("nodeId", node.getId());
		if (dataType != null) {
			query.setParameter("dataType", dataType);
		}
		return query.setMaxResults(count).getResultList();
	}

	@Override
	public void addHistory(NodeHistory history) {
		getJpaTemplate().getEntityManager().persist(history);
	}

	@Override
	public void cleanHistory(Date timepoint) {
		getJpaTemplate()
				.getEntityManager()
				.createNativeQuery(
						"delete from node_history where time <= ?")
				.setParameter(1, timepoint).executeUpdate();
	}

}
