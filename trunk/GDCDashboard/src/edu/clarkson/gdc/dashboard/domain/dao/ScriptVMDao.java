package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.common.proc.OutputHandler;
import edu.clarkson.gdc.common.proc.ProcessRunner;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public class ScriptVMDao implements VMDao {

	protected static final long REFRESH_INTERVAL = 60000;

	private String listScript;

	private String migrateScript;

	private Map<String, Long> lastRefresh;

	// Owner ID, VM name
	private Map<String, Map<String, VirtualMachine>> vms;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public ScriptVMDao() {
		super();
		vms = new ConcurrentHashMap<String, Map<String, VirtualMachine>>();
		lastRefresh = new HashMap<String, Long>();
	}

	protected void refresh(Machine owner, boolean force) {
		if (lastRefresh.containsKey(owner.getId())
				&& lastRefresh.get(owner.getId()) + REFRESH_INTERVAL > System
						.currentTimeMillis() && !force) {
			return;
		}
		synchronized (owner) {
			if (lastRefresh.containsKey(owner.getId())
					&& lastRefresh.get(owner.getId()) + REFRESH_INTERVAL > System
							.currentTimeMillis() && !force) {
				return;
			}
			try {
				ProcessRunner pr = new ProcessRunner(listScript, owner
						.getAttributes().get("ip"));
				ListVMHandler lvh = new ListVMHandler();
				pr.setHandler(lvh);
				pr.runAndWait();
				List<VirtualMachine> refreshed = lvh.getVms();
				Map<String, VirtualMachine> refreshedMap = new HashMap<String, VirtualMachine>();
				for (VirtualMachine newvm : refreshed) {
					refreshedMap.put(newvm.getName(), newvm);
				}
				vms.put(owner.getId(), refreshedMap);
				// Update Refresh Time
				lastRefresh.put(owner.getId(), System.currentTimeMillis());
			} catch (Exception e) {
				logger.error("Exception while executing refresh vm script", e);
			}
		}
	}

	@Override
	public void create(VirtualMachine vm) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void migrate(VirtualMachine vm, Machine source, Machine dest) {
		synchronized (source) {
			refresh(source, true);
			synchronized (dest) {
				if (!vms.get(source.getId()).containsKey(vm.getName()))
					return;
				ProcessRunner pr = new ProcessRunner(migrateScript, source
						.getAttributes().get("ip"), dest.getAttributes().get(
						"ip"), vm.getName());
				pr.setHandler(new MigrationHandler());
				try {
					pr.runAndWait();
					vms.get(dest.getId()).put(vm.getName(),
							vms.get(source.getId()).remove(vm.getName()));
				} catch (Exception e) {
					logger.error("Exception while executing migration script",
							e);
				}
				refresh(dest, true);
			}
		}
	}

	@Override
	public VirtualMachine find(Machine owner, String name) {
		refresh(owner, false);
		return vms.get(owner.getId()).get(name);
	}

	@Override
	public List<VirtualMachine> list(Machine owner) {
		List<VirtualMachine> result = new ArrayList<VirtualMachine>();
		result.addAll(vms.get(owner.getId()).values());
		Collections.sort(result, new Comparator<VirtualMachine>() {
			@Override
			public int compare(VirtualMachine o1, VirtualMachine o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return result;
	}

	public String getListScript() {
		return listScript;
	}

	public void setListScript(String listScript) {
		this.listScript = listScript;
	}

	public String getMigrateScript() {
		return migrateScript;
	}

	public void setMigrateScript(String migrateScript) {
		this.migrateScript = migrateScript;
	}

	protected static final class ListVMHandler implements OutputHandler {

		private List<VirtualMachine> vms;

		public ListVMHandler() {
			super();
			vms = new ArrayList<VirtualMachine>();
		}

		@Override
		public void output(String input) {
			throw new RuntimeException("Not implemented");
		}

		public List<VirtualMachine> getVms() {
			return vms;
		}
	}

	protected static final class MigrationHandler implements OutputHandler {
		@Override
		public void output(String input) {
			// Catch exception and throw out
		}
	}
}
