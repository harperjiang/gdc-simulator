package edu.clarkson.gdc.simulator.demoimpl;

import java.util.Stack;

public class AccessStack {

	public static AccessStack getInstance() {
		return instance;
	}
	
	private static AccessStack instance = new AccessStack();
	
	ThreadLocal<Stack<Object>> accessStacks;

	private AccessStack() {
		super();
		accessStacks = new ThreadLocal<Stack<Object>>() {
			@Override
			protected Stack<Object> initialValue() {
				return new Stack<Object>();
			}
		};
	}
	
	public Object peek() {
		return accessStacks.get().peek();
	}
	
	public void push(Object current) {
		accessStacks.get().push(current);
	}
	
	public void pop() {
		accessStacks.get().pop();
		
	}
}
