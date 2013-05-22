package edu.clarkson.gdc.simulator.framework.storage;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultData;

public class DefaultCacheStoragePerformanceTest {

	DefaultCacheStorage cache;

	DefaultCacheStorage memory;

	DefaultCacheStorage disk;

	@Before
	public void prepare() {
		cache = new DefaultCacheStorage();
		cache.setSize(100);
		cache.setDirtyThreshold(1);
		cache.setReadTime(10);
		cache.setWriteTime(10);
		cache.setWritePolicy(WritePolicy.WRITE_BACK);

		memory = new DefaultCacheStorage();
		cache.setInnerStorage(memory);
		memory.setSize(5000);
		memory.setDirtyThreshold(1);
		memory.setReadTime(200);
		memory.setWriteTime(200);
		memory.setWritePolicy(WritePolicy.WRITE_BACK);

		disk = new DefaultCacheStorage() {
			// Disk always contains data
			@Override
			public Pair<Long, Data> read(String key) {
				Pair<Long,Data> result = super.read(key);
				if(result.getB() == null) {
					Data data = new DefaultData(key);
					write(data);
					return new Pair<Long,Data>(result.getA(),data);
				}
				return result;
			}
		};
		memory.setInnerStorage(disk);
		disk.setSize(-1);
		disk.setReadTime(4000);
		disk.setWriteTime(1000);
	}

	@Test
	public void testPerformace() {
		Random random = new Random(System.currentTimeMillis()*this.hashCode());
		StringBuffer sb = null;
		long time = 0l;
		long count = 100000l;
		for(int i = 0 ; i < count;i++) {
			sb = new StringBuffer();
			sb.append('a'+random.nextInt(26));
			sb.append('a'+random.nextInt(26));
			Pair<Long,Data> result = cache.read(sb.toString());
			time+=result.getA();
		}
		System.out.println(time/count);
	}
}
