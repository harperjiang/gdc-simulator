package edu.clarkson.gdc.simulator.framework.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultData;

public class DefaultCacheStorageTest {

	DefaultCacheStorage cache;

	DefaultCacheStorage memory;

	DefaultCacheStorage disk;

	@Before
	public void prepare() {
		cache = new DefaultCacheStorage();
		cache.setSize(2);
		cache.setDirtyThreshold(2);
		cache.setReadTime(5);
		cache.setWriteTime(5);
		cache.setWritePolicy(WritePolicy.WRITE_BACK);

		memory = new DefaultCacheStorage();
		cache.setInnerStorage(memory);
		memory.setSize(10);
		memory.setDirtyThreshold(10);
		memory.setReadTime(50);
		memory.setWriteTime(50);
		memory.setWritePolicy(WritePolicy.WRITE_THROUGH);

		disk = new DefaultCacheStorage();
		memory.setInnerStorage(disk);
		disk.setSize(-1);
		disk.setReadTime(500);
		disk.setWriteTime(1000);
	}

	@Test
	public void testWriteData() {
		assertEquals(5, cache.write(new DefaultData("abc")));
		assertEquals(5, cache.write(new DefaultData("abc")));
		assertEquals(1055, cache.write(new DefaultData("def")));
		assertEquals(5, cache.write(new DefaultData("def")));

		assertEquals(1055, cache.write(new DefaultData("kkk")));
		assertEquals(5, cache.write(new DefaultData("kkk")));

		memory.setWritePolicy(WritePolicy.WRITE_BACK);

		assertEquals(55, cache.write(new DefaultData("pmk")));
		assertEquals(5, cache.write(new DefaultData("wku")));
		assertEquals(55, cache.write(new DefaultData("wnd")));
		assertEquals(5, cache.write(new DefaultData("ddw")));

		memory.setWritePolicy(WritePolicy.WRITE_THROUGH);

	}

	@Test
	public void testRead() {
		// Read a non existing data should not return null
		Pair<Long, Data> result = cache.read("aka");
		assertNotNull(result);
		assertEquals(new Long(555), result.getA());

		cache.setWritePolicy(WritePolicy.WRITE_THROUGH);
		for (int i = 0; i < 26; i++)
			cache.write(new DefaultData("" + i));
		result = cache.read("1");
		assertEquals(new Long(555), result.getA());
		result = cache.read("1");
		assertEquals(new Long(5), result.getA());
		result = cache.read("1");
		result = cache.read("25");
		assertEquals(new Long(5), result.getA());
		result = cache.read("2");
		assertEquals(new Long(555), result.getA());
		result = cache.read("2");
		assertEquals(new Long(5), result.getA());
		result = cache.read("1");
		assertEquals(new Long(55), result.getA());
	}

	@Test
	public void testException() {
		DefaultCacheStorage storage = new DefaultCacheStorage();
		storage.setSize(5);
		storage.write(new DefaultData("A"));
		storage.write(new DefaultData("B"));
		storage.write(new DefaultData("C"));
		storage.write(new DefaultData("D"));
		storage.write(new DefaultData("E"));
		try {
			storage.write(new DefaultData("F"));
			fail("Over Size");
		} catch (OutOfSpaceException e) {

		}
	}
}
