package edu.clarkson.gdc.simulator.framework;

import java.util.EventListener;
/**
 * 
 *
 *
 *
 * @author Hao Jiang
 * @since GDCSimulator 1.0
 * @version 1.0
 * 
 *
 */
public interface NodeStateListener extends EventListener {

	public void stateChanged(NodeStateEvent event);
}
