package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryLockDao implements LockDao {

	private Map<String, Lock> locks;

	public MemoryLockDao() {
		super();
		locks = new ConcurrentHashMap<String, Lock>();
	}

	@Override
	public boolean lock(String id) {
		if (!locks.containsKey(id)) {
			synchronized (locks) {
				if (!locks.containsKey(id)) {
					locks.put(id, new ReentrantLock());
				}
			}
		}
		return locks.get(id).tryLock();
	}

	@Override
	public void unlock(String id) {
		locks.get(id).unlock();
	}
}
