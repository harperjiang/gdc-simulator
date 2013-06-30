package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;

public interface AIService {
	
	public void relocateVM();

	public void handleAlert(Alert alert);
}
