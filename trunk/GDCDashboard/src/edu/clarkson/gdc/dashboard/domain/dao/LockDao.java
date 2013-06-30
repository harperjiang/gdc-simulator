package edu.clarkson.gdc.dashboard.domain.dao;

/**
 * Provide Locking Service
 * 
 * @author Hao Jiang
 * @since GDCDashboard 1.0
 * @version 1.0
 * 
 * @lastupdate 2013-06-29
 */
public interface LockDao {

	public boolean lock(String id);

	public void unlock(String id);
}
