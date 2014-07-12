package edu.clarkson.cs.gdc.network.common;

import javax.persistence.EntityManager;

public abstract class JpaDao {

	protected EntityManager getEntityManager() {
		return Environment.getEnvironment().getEntityManager();
	}

}
