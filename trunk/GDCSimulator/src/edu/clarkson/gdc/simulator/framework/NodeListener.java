package edu.clarkson.gdc.simulator.framework;

import java.util.EventListener;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */

public interface NodeListener extends EventListener {
	
	public void stateChanged(NodeStateEvent event);

	public void successReceived(NodeResponseEvent event);
	
	public void failureReceived(NodeResponseEvent event);
}
