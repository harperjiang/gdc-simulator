package edu.clarkson.gdc.dashboard.domain.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import edu.clarkson.gdc.common.ApplicationContextHolder;
import edu.clarkson.gdc.common.proc.OutputHandler;
import edu.clarkson.gdc.common.proc.ProcessRunner;
import edu.clarkson.gdc.common.proc.ProcessRunner.Callback;
import edu.clarkson.gdc.dashboard.domain.dao.vm.MigrationMutex;
import edu.clarkson.gdc.dashboard.domain.entity.Attributes;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.MigrationLog;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.VMService;

public class ScriptVMDao extends JpaDaoSupport implements VMDao {

	private long refreshInterval = 60000;

	private String listScript;

	private String migrateScript;

	private String operateScript;

	private Map<String, Long> lastRefresh;

	private boolean fastMigration = true;

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
					if (logger.isDebugEnabled()) {
						for (VirtualMachine vm : refreshed) {
							logger.debug(MessageFormat.format(
									"Got VM {0}:{1}",
									vm.getName(),
									vm.getAttributes().get(
											Attributes.VM_STATUS.attrName())));
						}
					}
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

	final private Set<MigrationMutex> mutex;

	@Override
	public void migrate(final VirtualMachine vm, final Machine source,
			final Machine dest) {
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
		try {
			String ip = Attributes.MACHINE_IP.attrName();
			ProcessRunner pr = null;
			if (fastMigration) {
				pr = new ProcessRunner(migrateScript, source.getAttributes()
						.get(ip), dest.getAttributes().get(ip), vm.getName(),
						"fast");
			} else {
				pr = new ProcessRunner(migrateScript, source.getAttributes()
						.get(ip), dest.getAttributes().get(ip), vm.getName());
			}

			MigrationLog log = new MigrationLog();
			log.setFromMachine(source.getId());
			log.setToMachine(dest.getId());
			log.setVmName(vm.getName());
			log.setBeginTime(new Date());
			getJpaTemplate().getEntityManager().persist(log);
			EntityManagerFactory nativeEmf = extract(getJpaTemplate()
					.getEntityManager().getEntityManagerFactory());
			if (log.getId() == 0 && JpaHelper.isEclipseLink(nativeEmf)) {
				ServerSession server = (ServerSession) JpaHelper
						.getServerSession(nativeEmf);
				server.getActiveUnitOfWork().assignSequenceNumber(log);
			}
			pr.setHandler(new MigrationHandler());
			pr.runLater(new MigrationCallback(source, dest, vm, log.getId()));
		} catch (RuntimeException e) {
			logger.error("Exception during migration", e);
			// Remove mutex
			synchronized (mutex) {
				mutex.remove(new MigrationMutex(source.getId(), dest.getId(),
						vm.getName()));
			}
			throw e;
		}
	}

	private EntityManagerFactory extract(
			EntityManagerFactory entityManagerFactory) {
		if (entityManagerFactory instanceof EntityManagerFactoryInfo) {
			return ((EntityManagerFactoryInfo) entityManagerFactory)
					.getNativeEntityManagerFactory();
		}
		return entityManagerFactory;
	}

	public class MigrationCallback implements Callback {

		private Machine source;

		private Machine dest;

		private VirtualMachine vm;

		private int logId;

		public MigrationCallback(Machine source, Machine dest,
				VirtualMachine vm, int logId) {
			this.source = source;
			this.dest = dest;
			this.vm = vm;
			this.logId = logId;
		}

		@Override
		public void done() {
			refresh(source, true);
			refresh(dest, true);
		}

		@Override
		public void exception(Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Failed to do migration",
					e);
		}

		@Override
		public void clean() {
			synchronized (mutex) {
				mutex.remove(new MigrationMutex(source.getId(), dest.getId(),
						vm.getName()));
			}
			VMService vmService = ApplicationContextHolder.getInstance()
					.getApplicationContext().getBean(VMService.class);
			vmService.migrationDone(logId);
		}

	}

	public void migrationDone(int logId) {
		try {
			EntityManager em = getJpaTemplate().getEntityManager();
			MigrationLog log = em
					.createQuery(
							"select m from MigrationLog m where m.id = :id",
							MigrationLog.class).setParameter("id", logId)
					.getSingleResult();
			log.setEndTime(new Date());
			em.merge(log);
		} catch (NoResultException e) {

		}
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

	public boolean isFastMigration() {
		return fastMigration;
	}

	public void setFastMigration(boolean fastMigration) {
		this.fastMigration = fastMigration;
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

		@Override
		public void output(String input) {
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

		@Override
		public void output(String input) {
		}
	}

	protected static final class OperateHandler implements OutputHandler {

		@Override
		public void output(String input) {

		}
	}

}
