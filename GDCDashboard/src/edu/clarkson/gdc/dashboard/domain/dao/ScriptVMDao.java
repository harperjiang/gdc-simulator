package edu.clarkson.gdc.dashboard.domain.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.common.proc.OutputHandler;
import edu.clarkson.gdc.common.proc.ProcessRunner;
import edu.clarkson.gdc.common.proc.ProcessRunner.Callback;
import edu.clarkson.gdc.dashboard.domain.entity.Attributes;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.VMService;

public class ScriptVMDao implements VMDao {

	private long refreshInterval = 60000;

	private String listScript;

	private String migrateScript;

	private String operateScript;

	private Map<String, Long> lastRefresh;

	// Owner ID, VM name
	private Map<String, Map<String, VirtualMachine>> vms;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public ScriptVMDao() {
		super();
		vms = new ConcurrentHashMap<String, Map<String, VirtualMachine>>();
		lastRefresh = new HashMap<String, Long>();

		mutex = new HashSet<MigrationMutex>();
	}

	protected void refresh(Machine owner, boolean force) {
		if (lastRefresh.containsKey(owner.getId())
				&& lastRefresh.get(owner.getId()) + getRefreshInterval() > System
						.currentTimeMillis() && !force) {
			return;
		}
		synchronized (owner) {
			if (lastRefresh.containsKey(owner.getId())
					&& lastRefresh.get(owner.getId()) + getRefreshInterval() > System
							.currentTimeMillis() && !force) {
				return;
			}
			try {
				String ip = owner.getAttributes().get(
						Attributes.MACHINE_IP.attrName());
				if (!StringUtils.isEmpty(ip)) {
					ProcessRunner pr = new ProcessRunner(listScript, ip);
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
				}
			} catch (Exception e) {
				logger.error("Exception while executing refresh vm script", e);
			}
		}
	}

	@Override
	public void create(VirtualMachine vm) {
		throw new RuntimeException("Not implemented");
	}

	private static final class MigrationMutex {
		String sourceId;
		String destId;
		String vmName;

		public MigrationMutex(String s, String d, String v) {
			this.sourceId = s;
			this.destId = d;
			this.vmName = v;
		}

		@Override
		public int hashCode() {
			return (sourceId + ":" + destId + ":" + vmName).hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MigrationMutex) {
				MigrationMutex mm = (MigrationMutex) obj;
				return mm.getSourceId().equals(sourceId)
						&& mm.getDestId().equals(getDestId())
						&& mm.getVmName().equals(getVmName());
			}
			return super.equals(obj);
		}

		public String getSourceId() {
			return sourceId;
		}

		public String getDestId() {
			return destId;
		}

		public String getVmName() {
			return vmName;
		}

	}

	private Set<MigrationMutex> mutex;

	@Override
	public void migrate(VirtualMachine vm, Machine source, final Machine dest) {
		// TODO Migration is currently done asynchronously. Should do something
		// to prevent duplicated operation. Current solution is only valid for
		// single machine
		synchronized (mutex) {
			MigrationMutex mm = new MigrationMutex(source.getId(),
					dest.getId(), vm.getName());
			if (mutex.contains(mm))
				throw new IllegalStateException(MessageFormat.format(
						"Already in migration:{0}--{2}->{1}", source.getId(),
						vm.getName(), dest.getId()));
			mutex.add(mm);
		}
		String ip = Attributes.MACHINE_IP.attrName();
		ProcessRunner pr = new ProcessRunner(migrateScript, source
				.getAttributes().get(ip), dest.getAttributes().get(ip),
				vm.getName());
		pr.setHandler(new MigrationHandler());
		pr.runLater(new Callback() {
			@Override
			public void done() {
				refresh(dest, true);
			}

			@Override
			public void exception(Exception e) {
				LoggerFactory.getLogger(getClass()).warn(
						"Failed to do migration", e);
			}
		});
	}

	@Override
	public VirtualMachine find(Machine owner, String name) {
		refresh(owner, false);
		return vms.get(owner.getId()).get(name);
	}

	@Override
	public List<VirtualMachine> list(Machine owner) {
		refresh(owner, false);
		List<VirtualMachine> result = new ArrayList<VirtualMachine>();
		if (vms.containsKey(owner.getId())) {
			result.addAll(vms.get(owner.getId()).values());
			Collections.sort(result, new Comparator<VirtualMachine>() {
				@Override
				public int compare(VirtualMachine o1, VirtualMachine o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		return result;
	}

	@Override
	public void operate(Machine owner, VirtualMachine vm,
			VMService.Operation operation) {
		try {
			ProcessRunner runner = new ProcessRunner(getOperateScript(), owner
					.getAttributes().get(Attributes.MACHINE_IP.attrName()),
					operation.getOperand(), vm.getName());
			runner.setHandler(new OperateHandler());
			runner.runAndWait();
		} catch (Exception e) {
			logger.error(MessageFormat.format(
					"Exception while doing operation {0} on {1}",
					operation.name(), vm.getName()), e);
		}
		refresh(owner, true);
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

	public long getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public String getOperateScript() {
		return operateScript;
	}

	public void setOperateScript(String operateScript) {
		this.operateScript = operateScript;
	}

	protected static final class ListVMHandler implements OutputHandler {

		private List<VirtualMachine> vms;

		private int counter = -1;

		private static final Pattern start = Pattern
				.compile("[\\t ]+Id[\\t ]+Name[\\t ]+State");

		private static final Pattern pattern = Pattern
				.compile("[\\t ]*([\\w\\-]+)[\\t ]+([\\w\\-]+)[\\t ]+([\\w\\- ]+)");

		public ListVMHandler() {
			super();
			vms = new ArrayList<VirtualMachine>();
		}

		private Logger logger = LoggerFactory.getLogger(getClass());

		@Override
		public void output(String input) {
			if (logger.isDebugEnabled()) {
				logger.debug(input);
			}
			try {
				if (start.matcher(input).matches()) {
					counter = 0;
				}
				// Skip 2 lines
				if (counter >= 2) {
					Matcher matcher = pattern.matcher(input);
					if (matcher.matches()) {
						VirtualMachine vm = new VirtualMachine();
						vm.setName(matcher.group(2));
						vm.getAttributes().put(Attributes.VM_STATUS.attrName(),
								matcher.group(3));
						vms.add(vm);
					}
				}
			} finally {
				if (counter >= 0)
					counter++;
			}
		}

		public List<VirtualMachine> getVms() {
			return vms;
		}
	}

	protected static final class MigrationHandler implements OutputHandler {

		private Logger logger = LoggerFactory.getLogger(getClass());

		@Override
		public void output(String input) {
			// There should be no output
			this.logger
					.error("Exception while doing script migration:" + input);
		}
	}

	protected static final class OperateHandler implements OutputHandler {

		private Logger logger = LoggerFactory.getLogger(getClass());

		@Override
		public void output(String input) {
			if (logger.isDebugEnabled()) {
				logger.debug(input);
			}
		}
	}

}
