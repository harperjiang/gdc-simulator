package edu.clarkson.gdc.simulator.framework;

import java.util.EventListener;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */

public interface NodeMessageListener extends EventListener {

	public void messageReceived(NodeMessageEvent event);

	public void messageSent(NodeMessageEvent event);
	
	public void messageTimeout(NodeMessageEvent event);
}
