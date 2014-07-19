package edu.clarkson.cs.network.ipinfo.api;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.queries.ScrollableCursor;

import edu.clarkson.cs.network.common.JpaDao;
import edu.clarkson.cs.network.ipinfo.model.IPInfo;

public class IPInfoDao extends JpaDao {

	public IPInfo find(String ip) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<IPInfo> cquery = builder.createQuery(IPInfo.class);
		Root<IPInfo> root = cquery.from(IPInfo.class);
		cquery.where(builder.equal(root.get("ip"), ip));
		try {
			IPInfo info = getEntityManager().createQuery(cquery)
					.getSingleResult();
			return info;
		} catch (NoResultException e) {
			return null;
		}
	}

	public void save(IPInfo info) {
		EntityTransaction transaction = getEntityManager().getTransaction();
		// Insert this item
		transaction.begin();
		getEntityManager().persist(info);
		transaction.commit();
	}

	public ScrollableCursor all() {
		return (ScrollableCursor) getEntityManager()
				.createQuery("select i from IPInfo i")
				.setHint("eclipselink.cursor.scrollable", true)
				.getSingleResult();
	}
}
