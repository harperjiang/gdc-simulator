package edu.clarkson.gdc.simulator.framework.storage;

import java.util.Collection;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;

public interface Storage {

	/**
	 * General Read Time
	 * @return
	 */
	public long getReadTime();

	/**
	 * General Write Time
	 * @return
	 */
	public long getWriteTime();

	public long write(Data data);

	public long write(Collection<Data> datas);

	public Pair<Long, Data> read(String key);
	
	public long getRemaining();
}
