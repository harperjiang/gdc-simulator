package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.AlertLevel;

public interface AlertDao {

	void save(Alert alert);

	List<Alert> getAlerts(AlertLevel level);

}
