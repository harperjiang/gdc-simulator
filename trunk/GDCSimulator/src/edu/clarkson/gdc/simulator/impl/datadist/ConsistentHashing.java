package edu.clarkson.gdc.simulator.impl.datadist;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsistentHashing {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Random random;

	private int scale = 40;

	// How many locations one server will manage on the circle
	private int base = 10;

	private int compressTrigger = 20;

	protected List<PositionHolder> positions;

	protected Map<String, List<PositionHolder>> index;

	public ConsistentHashing() {
		super();
		random = new Random(System.currentTimeMillis());

		positions = new ArrayList<PositionHolder>();
		index = new HashMap<String, List<PositionHolder>>();
	}

	// Arrange hashing cycle
	public void add(String node) {
		Validate.isTrue(!index.containsKey(node));
		List<PositionHolder> indexList = new ArrayList<PositionHolder>();
		for (int i = 0; i < base; i++) {
			BigDecimal number = new BigDecimal(random.nextDouble()).setScale(
					scale, BigDecimal.ROUND_HALF_UP);
			Position inst = new Position(number, node);
			PositionHolder holder = new PositionHolder(inst);
			int position = locate(positions, inst, 0, positions.size());
			if (position == positions.size())
				positions.add(holder);
			else
				positions.add(position, holder);
			indexList.add(holder);
		}
		index.put(node, indexList);
	}

	protected int hole = 0;

	public void remove(String node) {
		// For performance consideration,do compression regular
		Validate.isTrue(index.containsKey(node));
		List<PositionHolder> holders = index.get(node);
		for (PositionHolder holder : holders) {
			// Make a hole
			holder.position = null;
			hole++;
		}
		if (positions.size() / hole < compressTrigger)
			// Compress, removing hole
			compress();
	}

	public Set<String> get(String key, int count) {
		if (count > index.size() * base)
			throw new IllegalArgumentException("Cannot satisfy");
		BigDecimal hash = hash(key);
		int position = locate(positions, new Position(hash, key), 0,
				positions.size());
		Set<String> result = new HashSet<String>();
		int index = position;
		int counter = 0;
		while (result.size() < count) {
			if (positions.get(index).position != null) {
				result.add(positions.get(index).position.id);
				counter++;
			}
			index = (index + 1) % positions.size();
		}
		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format(
					"Tried {0} times to get {1} instance(s)", counter, count));
		}
		return result;
	}

	protected BigDecimal hash(String key) {
		return new BigDecimal(new Random(key.hashCode()).nextDouble())
				.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	protected void compress() {
		List<PositionHolder> newlist = new ArrayList<PositionHolder>();
		for (PositionHolder ph : positions) {
			if (ph.position != null)
				newlist.add(ph);
		}
		positions = newlist;
		hole = 0;
	}

	// Locate a valid location to insert the new node
	protected static int locate(List<PositionHolder> list, Position inst,
			int begin, int end) {
		if (list.isEmpty()) // empty list
			return begin;
		if (begin == end) {
			return begin;
		}
		// Subtract start and end to meet a non-empty value
		while (begin < end && list.get(begin).position == null)
			begin++;
		while (end > begin && list.get(end - 1).position == null)
			end--;
		if (begin == end - 1) { // Single element
			if (list.get(begin).position == null) {// total empty
				return begin;
			} else {
				int compare = inst.compareTo(list.get(begin).position);
				return (compare <= 0) ? begin : begin + 1;
			}
		}
		// List, separate
		int middle = (begin + end) / 2;
		if (list.get(middle).position == null) {
			// Middle is empty, have to search around to get one
			int lindex = middle, rindex = middle;
			while (lindex >= begin && list.get(lindex).position == null)
				lindex--;
			while (rindex < end && list.get(rindex).position == null)
				rindex++;
			// lindex will surely point to some non-null value.
			if (rindex == end)
				return locate(list, inst, begin, lindex + 1);

			int lcomp = inst.compareTo(list.get(lindex).position);
			int rcomp = inst.compareTo(list.get(rindex).position);
			if (lcomp < 0)
				return locate(list, inst, begin, lindex);
			if (lcomp >= 0 && rcomp < 0)
				return locate(list, inst, lindex, rindex);
			if (lcomp > 0 && rcomp <= 0)
				return locate(list, inst, lindex + 1, rindex + 1);
			return locate(list, inst, rindex, end);
		}
		// Otherwise, just directly compare
		int compare = inst.compareTo(list.get(middle).position);
		if (compare < 0)
			return locate(list, inst, begin, middle);
		if (compare > 0)
			return locate(list, inst, middle, end);
		return middle;// insert just before this location

	}

	public int getCompressTrigger() {
		return compressTrigger;
	}

	public void setCompressTrigger(int compressTrigger) {
		this.compressTrigger = compressTrigger;
	}

	public int getBase() {
		return base;
	}

	public List<PositionHolder> getPositions() {
		return positions;
	}

	public Map<String, List<PositionHolder>> getIndex() {
		return index;
	}

	protected static final class PositionHolder {
		private Position position;

		public PositionHolder(Position position) {
			this.position = position;
		}

		public Position getPosition() {
			return position;
		}

		public void setPosition(Position position) {
			this.position = position;
		}

		public String toString() {
			return (null == position) ? "NULL" : position.toString();
		}
	}

	protected static final class Position implements Comparable<Position> {

		BigDecimal value;

		String id;

		public Position(BigDecimal v, String i) {
			this.value = v;
			this.id = i;
		}

		public BigDecimal getValue() {
			return value;
		}

		public String getId() {
			return id;
		}

		@Override
		public int compareTo(Position another) {
			return this.value.compareTo(another.value);
		}

		@Override
		public String toString() {
			return MessageFormat.format("{0}:{1}", getValue().toString(),
					getId());
		}
	}
}
