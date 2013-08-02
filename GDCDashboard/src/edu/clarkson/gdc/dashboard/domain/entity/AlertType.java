package edu.clarkson.gdc.dashboard.domain.entity;

public enum AlertType {

	POWER_TOO_LOW(AlertLevel.SEVERE), POWER_IS_HIGH(AlertLevel.INFO), POWER_CHECK_SENSOR(
			AlertLevel.WARNING);

	private AlertLevel level;

	AlertType(AlertLevel level) {
		this.level = level;
	}

	public AlertLevel level() {
		return this.level;
	}
}
