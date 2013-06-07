package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;

public class JpaStatusDao extends JpaDaoSupport implements StatusDao {

	@Override
	public NodeStatus getStatus(Node node, String dataType) {
		try {
			return getJpaTemplate()
					.getEntityManager()
					.createQuery(
							"select s from NodeStatus s where s.nodeId = :id and s.dataType = :dt",
							NodeStatus.class).setParameter("id", node.getId())
					.setParameter("dt", dataType).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Map<String, NodeStatus> getStatus(Node node) {
		List<NodeStatus> resultList = getJpaTemplate()
				.getEntityManager()
				.createQuery("select s from NodeStatus s where s.nodeId = :id",
						NodeStatus.class).setParameter("id", node.getId())
				.getResultList();
		Map<String, NodeStatus> result = new HashMap<String, NodeStatus>();
		for (NodeStatus ns : resultList) {
			result.put(ns.getDataType(), ns);
		}
		return result;
	}

	@Override
	public void updateStatus(NodeStatus status) {
		getJpaTemplate().getEntityManager().merge(status);
	}

}
