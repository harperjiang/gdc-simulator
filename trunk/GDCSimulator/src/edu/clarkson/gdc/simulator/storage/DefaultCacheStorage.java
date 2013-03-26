package edu.clarkson.gdc.simulator.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;

public class DefaultCacheStorage implements NestedStorage {

	private long readTime;

	private long writeTime;

	private Storage innerStorage;

	private long size = -1;

	// TODO Extract write policy as separated interface
	private WritePolicy writePolicy;

	private Map<String, Data> index;

	private List<String> recentUsed;

	private long dirtyThreshold;

	private Set<String> dirtyRecords;

	public DefaultCacheStorage() {
		super();
		this.index = new HashMap<String, Data>();

		recentUsed = new ArrayList<String>();

		dirtyThreshold = 1;
		dirtyRecords = new HashSet<String>();
	}

	@Override
	public long write(Data data) {
		if (!index.containsKey(data.getKey())) {
			// Store the data
			if (index.size() == size) {
				String old = recentUsed.remove(0);
				index.remove(old);
			}
			index.put(data.getKey(), data);
		}
		// Update recent used
		recentUsed.remove(data.getKey());
		recentUsed.add(data.getKey());

		if (WritePolicy.WRITE_THROUGH == getWritePolicy()) {
			if (getInnerStorage() != null) {
				return getWriteTime() + getInnerStorage().write(data);
			}
			return getWriteTime();
		} else {
			dirtyRecords.add(data.getKey());
			if (getInnerStorage() != null
					&& dirtyRecords.size() == dirtyThreshold) {
				List<Data> dirtyDatas = new ArrayList<Data>();
				for (String key : dirtyRecords)
					dirtyDatas.add(index.get(key));
				long writeTime = getInnerStorage().write(dirtyDatas);
				dirtyRecords.clear();
				return getWriteTime() + writeTime;
			}
			return getWriteTime();
		}
	}

	@Override
	public long write(Collection<Data> datas) {
		long max = -1;
		for (Data data : datas)
			max = Math.max(max, write(data));
		return max;
	}

	@Override
	public Pair<Long, Data> read(String key) {
		if (index.containsKey(key)) {
			recentUsed.remove(key);
			recentUsed.add(key);
			return new Pair<Long, Data>(getReadTime(), index.get(key));
		}
		if (null == getInnerStorage())
			return null;
		Pair<Long, Data> result = getInnerStorage().read(key);

		// Store data in cache
		if (index.size() == size) {
			String old = recentUsed.remove(0);
			index.remove(old);
		}
		index.put(result.getB().getKey(), result.getB());
		recentUsed.add(result.getB().getKey());

		return new Pair<Long, Data>(result.getA() + getReadTime(),
				result.getB());
	}

	public long getReadTime() {
		return readTime;
	}

	public void setReadTime(long readTime) {
		this.readTime = readTime;
	}

	public long getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(long writeTime) {
		this.writeTime = writeTime;
	}

	public Storage getInnerStorage() {
		return innerStorage;
	}

	public void setInnerStorage(Storage innerStorage) {
		this.innerStorage = innerStorage;
	}

	public long getDirtyThreshold() {
		return dirtyThreshold;
	}

	public void setDirtyThreshold(long dirtyThreshold) {
		this.dirtyThreshold = dirtyThreshold;
	}

	public WritePolicy getWritePolicy() {
		return writePolicy;
	}

	public void setWritePolicy(WritePolicy writePolicy) {
		this.writePolicy = writePolicy;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
