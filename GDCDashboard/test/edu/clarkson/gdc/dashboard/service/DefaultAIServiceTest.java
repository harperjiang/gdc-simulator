package edu.clarkson.gdc.dashboard.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import edu.clarkson.gdc.dashboard.domain.dao.MemoryVMDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.AlertType;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

@ContextConfiguration(locations = { "/application-context.xml" })
public class DefaultAIServiceTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private AIService aiService;

	@Resource(name = "memoryVmDao")
	private MemoryVMDao vmDao;

	@Test
	public void testRelocateVM() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleAlert() {
		Alert alert = new Alert();
		alert.setNodeId("dc2-power");
		alert.setType(AlertType.POWER_TOO_LOW);
		alert.setLevel(AlertType.POWER_TOO_LOW.level());
		aiService.handleAlert(alert);

		List<Object[]> migrations = vmDao.getMigrationHistory();
		assertEquals(1, migrations.size());
		VirtualMachine vm = (VirtualMachine) migrations.get(0)[0];
		Machine from = (Machine) migrations.get(0)[1];
		Machine to = (Machine) migrations.get(0)[2];
		assertEquals("Good VM from dc2-bty1-mc1", vm.getName());
		assertEquals("dc2-bty1-mc1", from.getId());
		assertEquals("dc1-bty1-mc1", to.getId());

		vmDao.getMigrationHistory().clear();

		alert = new Alert();
		alert.setNodeId("dc2-power");
		alert.setType(AlertType.POWER_IS_HIGH);
		alert.setLevel(AlertType.POWER_IS_HIGH.level());
		aiService.handleAlert(alert);

		migrations = vmDao.getMigrationHistory();
		assertEquals(2, migrations.size());
		vm = (VirtualMachine) migrations.get(0)[0];
		from = (Machine) migrations.get(0)[1];
		to = (Machine) migrations.get(0)[2];
		assertEquals("Good VM from dc1-bty1-mc1", vm.getName());
		assertEquals("dc1-bty1-mc1", from.getId());
		assertEquals("dc2-bty1-mc1", to.getId());

		vm = (VirtualMachine) migrations.get(1)[0];
		from = (Machine) migrations.get(1)[1];
		to = (Machine) migrations.get(1)[2];
		assertEquals("Good VM from dc2-bty1-mc1", vm.getName());
		assertEquals("dc1-bty1-mc1", from.getId());
		assertEquals("dc2-bty1-mc1", to.getId());
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
