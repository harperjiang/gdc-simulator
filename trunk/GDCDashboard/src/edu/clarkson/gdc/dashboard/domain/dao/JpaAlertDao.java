package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;

public class JpaAlertDao extends JpaDaoSupport implements AlertDao {

	@Override
	public List<Alert> getAlerts() {
		return getJpaTemplate()
				.getEntityManager()
				.createQuery("select a from Alert a order by a.time desc",
						Alert.class).setMaxResults(20).getResultList();
	}

	@Override
	public void save(Alert alert) {
		if (alert.getId() == 0)
			getJpaTemplate().getEntityManager().persist(alert);
		else
			getJpaTemplate().getEntityManager().merge(alert);
	}

}
