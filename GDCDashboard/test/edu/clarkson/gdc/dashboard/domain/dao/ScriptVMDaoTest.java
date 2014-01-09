package edu.clarkson.gdc.dashboard.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

@ContextConfiguration(locations = "/application-context.xml")
public class ScriptVMDaoTest extends AbstractJUnit4SpringContextTests {

	@Resource
	ScriptVMDao vmDao;

	@Test
	public void testCreate() {
		fail("Not yet implemented");
	}

	@Test
	public void testMigrate() {

	}

	@Test
	public void testFind() {
		Machine owner = new Machine();
		owner.setId("aka");
		owner.getAttributes().put("ip", "123");
		assertNotNull(vmDao.find(owner, "123-vm-02"));
		assertNull(vmDao.find(owner, "123-vm-002"));
	}

	@Test
	public void testList() {
		Machine owner = new Machine();
		owner.setId("aka");
		owner.getAttributes().put("ip", "123");
		List<VirtualMachine> vms = vmDao.list(owner);
		assertEquals(3, vms.size());
		assertEquals("123-vm-01", vms.get(0).getName());
		assertEquals("123-vm-02", vms.get(1).getName());
		assertEquals("123-vm-03", vms.get(2).getName());
		assertEquals("running", vms.get(0).getAttributes().get("status"));
		assertEquals("running", vms.get(1).getAttributes().get("status"));
		assertEquals("stopped", vms.get(2).getAttributes().get("status"));
	}

	@Test
	public void testRemoteList() {
		Machine owner = new Machine();
		owner.setId("ABC");
		owner.getAttributes().put("ip", "128.153.145.175");
		vmDao.setListScript("/home/harper/GDC/display.sh");
		List<VirtualMachine> vms = vmDao.list(owner);
		assertEquals(1, vms.size());
	}
}
