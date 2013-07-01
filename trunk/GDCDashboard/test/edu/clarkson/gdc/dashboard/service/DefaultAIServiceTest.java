package edu.clarkson.gdc.dashboard.service;

import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.AlertType;

@ContextConfiguration(locations = { "/application-context.xml" })
public class DefaultAIServiceTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private AIService aiService;

	@Test
	public void testRelocateVM() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleAlert() {
		Alert alert = new Alert();
		alert.setNodeId("dc2-power");
		alert.setType(AlertType.BTY_LOW_LEVEL);
		alert.setLevel(AlertType.BTY_LOW_LEVEL.level());
		aiService.handleAlert(alert);
	}

	@Test
	public void testMigrateOut() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeMigrateDecision() {
		fail("Not yet implemented");
	}

	@Test
	public void testScore() {
		fail("Not yet implemented");
	}

	@Test
	public void testScore2() {
		fail("Not yet implemented");
	}

}
