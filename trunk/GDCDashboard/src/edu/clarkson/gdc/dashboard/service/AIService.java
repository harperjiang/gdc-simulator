package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;

public interface AIService {

	public void updateStatus();
	
	public void makeMigrationDecision();
	
	public void handleAlert(Alert alert);
}
