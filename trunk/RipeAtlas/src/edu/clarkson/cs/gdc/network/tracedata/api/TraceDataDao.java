package edu.clarkson.cs.gdc.network.tracedata.api;

import javax.persistence.EntityManager;

import org.eclipse.persistence.queries.ScrollableCursor;

import edu.clarkson.cs.gdc.network.common.JpaDao;

public class TraceDataDao extends JpaDao {

	public ScrollableCursor allData() {
		EntityManager em = getEntityManager();
		ScrollableCursor cursor = (ScrollableCursor) em
				.createQuery("select t from TraceData t")
				.setHint("eclipselink.cursor.scrollable", true)
				.getSingleResult();
		return cursor;
	}
}
