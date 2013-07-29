package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.MigrationLog;

public interface MigrationLogDao {

	public List<MigrationLog> getUnfinished();
	
	public List<MigrationLog> getLatest(long interval);
}
