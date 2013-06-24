package edu.clarkson.gdc.dashboard.domain.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;

public class XMLNodeDaoTest {

	@Test
	public void testUp() {
		XMLNodeDao dao = new XMLNodeDao();
		Machine m = dao.getNode("dc1-bty1-mc1");
		assertNotNull(dao.up(m, DataCenter.class));
	}

}
