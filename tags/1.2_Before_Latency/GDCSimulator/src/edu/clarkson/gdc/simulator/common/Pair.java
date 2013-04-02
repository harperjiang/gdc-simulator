package edu.clarkson.gdc.simulator.common;

import java.text.MessageFormat;

public class Pair<A, B> {

	private A a;

	private B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}

	public String toString() {
		return MessageFormat.format("({0},{1})", String.valueOf(a),
				String.valueOf(b));
	}

	public boolean equals(Object another) {
		if (another instanceof Pair) {
			Pair<?, ?> ap = (Pair<?, ?>) another;
			return a.equals(ap.getA()) && b.equals(ap.getB());
		}
		return super.equals(another);
	}

	public int hashCode() {
		return a.hashCode() * 43 + b.hashCode();
	}
}
