package gdc.network.model;

import java.util.Arrays;

public class Key implements Comparable<Key> {

	private String[] data;

	public Key(String... data) {
		this.data = data;
	}

	@Override
	public int compareTo(Key o) {
		if (this == o)
			return 0;
		if (data == null || o.data == null) {
			if (data == o.data)
				return 0;
			if (data == null)
				return 1;
			return -1;
		}
		int index = 0;
		int range = Math.min(data.length, data.length);
		for (index = 0; index < range; index++) {
			int result;
			if ((result = data[index].compareTo(o.data[index])) != 0)
				return result;
		}
		if (data.length == range && o.data.length == range)
			return 0;
		if (data.length > range)
			return 1;
		return -1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}
}
