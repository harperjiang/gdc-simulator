package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;

public interface AlertDao {

	void save(Alert alert);
	
	List<Alert> getAlerts();

}
