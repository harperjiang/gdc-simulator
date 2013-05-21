package edu.clarkson.gdc.simulator.scenario.tact;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.clarkson.gdc.simulator.scenario.tact.SortingList;

public class SortingListTest {

	@Test
	public void testAdd() {
		SortingList<Long> sorting = new SortingList<Long>();

		sorting.add(3l);
		sorting.add(1l);
		sorting.add(5l);
		sorting.add(2l);
		sorting.add(6l);
		sorting.add(7l);
		sorting.add(6l);
		sorting.add(2l);
		sorting.add(7l);
		sorting.add(1l);

		assertEquals(1l, sorting.getList().get(0).longValue());
		assertEquals(1l, sorting.getList().get(1).longValue());
		assertEquals(2l, sorting.getList().get(2).longValue());
		assertEquals(2l, sorting.getList().get(3).longValue());
		assertEquals(3l, sorting.getList().get(4).longValue());
		assertEquals(5l, sorting.getList().get(5).longValue());
		assertEquals(6l, sorting.getList().get(6).longValue());
		assertEquals(6l, sorting.getList().get(7).longValue());
		assertEquals(7l, sorting.getList().get(8).longValue());
		assertEquals(7l, sorting.getList().get(9).longValue());
	}

	@Test
	public void testPositionBinary() {
		SortingList<Long> sorting = new SortingList<Long>();
		assertEquals(0, sorting.position_binary(5l));
		assertEquals(0, sorting.position_postlinear(5l));
		sorting.add(10l);
		assertEquals(0, sorting.position_binary(5l));
		assertEquals(0, sorting.position_postlinear(5l));
		sorting.add(5l);
		assertEquals(1, sorting.position_binary(5l));
		assertEquals(1, sorting.position_postlinear(5l));
		sorting.add(5l);
		assertEquals(2, sorting.position_binary(5l));
		assertEquals(2, sorting.position_postlinear(5l));

	}

}
