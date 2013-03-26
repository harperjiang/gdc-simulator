package edu.clarkson.gdc.simulator.storage;

import java.util.Collection;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;

public interface Storage {

	public long getReadTime();

	public long getWriteTime();

	public long write(Data data);

	public long write(Collection<Data> datas);

	public Pair<Long, Data> read(String key);
}
