package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.AlertLevel;

public class JpaAlertDao extends JpaDaoSupport implements AlertDao {

	@Override
	public List<Alert> getAlerts(AlertLevel level) {
		if (level == null) {
			return getJpaTemplate()
					.getEntityManager()
					.createQuery("select a from Alert a order by a.time desc",
							Alert.class).setMaxResults(20).getResultList();
		}
		return getJpaTemplate()
				.getEntityManager()
				.createQuery(
						"select a from Alert a where a.level = :level order by a.time desc",
						Alert.class).setParameter("level", level.name())
				.setMaxResults(20).getResultList();
	}

	@Override
	public void save(Alert alert) {
		if (alert.getId() == 0)
			getJpaTemplate().getEntityManager().persist(alert);
		else
			getJpaTemplate().getEntityManager().merge(alert);
	}

}
