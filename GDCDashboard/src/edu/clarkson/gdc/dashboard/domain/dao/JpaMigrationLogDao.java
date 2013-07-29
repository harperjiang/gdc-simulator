package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Date;
import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import edu.clarkson.gdc.dashboard.domain.entity.MigrationLog;

public class JpaMigrationLogDao extends JpaDaoSupport implements
		MigrationLogDao {

	@Override
	public List<MigrationLog> getUnfinished() {
		return getJpaTemplate()
				.getEntityManager()
				.createQuery(
						"select m from MigrationLog m where m.endTime is null order by m.beginTime desc",
						MigrationLog.class).getResultList();
	}

	@Override
	public List<MigrationLog> getLatest(long interval) {
		return getJpaTemplate()
				.getEntityManager()
				.createQuery(
						"select m from MigrationLog m where m.beginTime >= :time order by m.beginTime desc",
						MigrationLog.class)
				.setParameter("time",
						new Date(System.currentTimeMillis() - interval))
				.getResultList();
	}

}
