package edu.clarkson.cs.network.ipinfo.api;

import java.util.List;

import edu.clarkson.cs.network.common.JpaDao;
import edu.clarkson.cs.network.ipinfo.model.RingNode;

public class RingNodeDao extends JpaDao {

	public List<RingNode> allatonce() {
		return getEntityManager().createQuery("select rn from RingNode rn",
				RingNode.class).getResultList();
	}

	public List<RingNode> incountry(String country) {
		return getEntityManager()
				.createQuery(
						"select rn from RingNode rn where rn.country = :country",
						RingNode.class).setParameter("country", country)
				.getResultList();
	}
}
