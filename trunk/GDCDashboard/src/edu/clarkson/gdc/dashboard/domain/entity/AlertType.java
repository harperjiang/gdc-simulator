package edu.clarkson.gdc.dashboard.domain.entity;

public enum AlertType {

	BTY_TOO_LOW(AlertLevel.SEVERE), BTY_IS_HIGH(AlertLevel.INFO), BTY_CHECK_SENSOR(
			AlertLevel.WARNING);

	private AlertLevel level;

	AlertType(AlertLevel level) {
		this.level = level;
	}

	public AlertLevel level() {
		return this.level;
	}
}
