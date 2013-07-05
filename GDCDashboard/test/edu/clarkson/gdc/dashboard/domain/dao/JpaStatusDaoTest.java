package edu.clarkson.gdc.dashboard.domain.dao;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import edu.clarkson.gdc.dashboard.domain.entity.Battery;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;

@ContextConfiguration(locations = { "/application-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class JpaStatusDaoTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Resource
	StatusDao statusDao;

	@Before
	public void prepare() {
		NodeStatus status = new NodeStatus();
		status.setNodeId("aak");
		status.setDataType(StatusType.MACHINE_CPU.name());
		status.setValue("apa");

		statusDao.updateStatus(status);
	}

	@Test
	public void test() {
		Node node = new Battery();
		node.setId("aak");
		NodeStatus status = statusDao.getStatus(node, StatusType.MACHINE_CPU);
		assertEquals("apa", status.getValue());
	}

}
