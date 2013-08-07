package edu.clarkson.gdc.dashboard.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import edu.clarkson.gdc.dashboard.domain.entity.Battery;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;

@ContextConfiguration(locations = { "/application-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class JpaHistoryDaoTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Resource
	HistoryDao historyDao;

	@Before
	public void prepare() {
		NodeHistory history = new NodeHistory();
		history.setNodeId("aka");
		history.setDataType("ddt");
		history.setValue("ababa");
		history.setTime(new Date());

		historyDao.addHistory(history);
	}

	@Test
	public void test() {
		Battery node = new Battery();
		node.setId("aka");
		List<NodeHistory> hists = historyDao.getHistories(node, "ddt", 1);
		assertEquals(1,hists.size());
		NodeHistory hist = hists.get(0);
		assertTrue(hist.getId() != 0);
	}
	
	@Test
	public void testDelete() {
		historyDao.cleanHistory(new Date());
	}

}
