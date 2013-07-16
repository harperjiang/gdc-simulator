package edu.clarkson.gdc.dashboard.domain.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.AlertLevel;
import edu.clarkson.gdc.dashboard.domain.entity.AlertType;

@ContextConfiguration(locations = { "/application-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class JpaAlertDaoTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Resource(name = "alertDao")
	AlertDao alertDao;

	@Before
	public void prepare() {
		Alert alert = new Alert();
		alert.setNodeId("node1");
		alert.setNodeName("type");
		alert.setType(AlertType.BTY_TOO_LOW);
		alert.setTime(new Date());
		alert.setLevel(AlertLevel.SEVERE);

		alertDao.save(alert);
	}

	@Test
	public void testGet() {
		List<Alert> alerts = alertDao.getAlerts(AlertLevel.SEVERE);
		assertEquals(1, alerts.size());
	}

}
