package de.hpi.oryxengine.navigator;

import de.hpi.oryxengine.node.AbstractNode;

/**
 * The Interface NavigatorInterface.
 */
public interface NavigatorInterface {

	/**
	 * Start a new process instance.
	 *
	 * @param processID the id of the process that is to be instanciated
	 * @return the id of the newly created process instance
	 */
	public String startProcessInstance(String processID);
	
	/**
	 * Stop process instance.
	 *
	 * @param instanceID the id of the instance that is to be stopped
	 */
	public void stopProcessInstance(String instanceID);
	
	public String getCurrentInstanceState(String instanceID);
	
	/**
	 * Signal the finished execution of a node.
	 *
	 * @param node the node that finished execution
	 */
	void signal(AbstractNode node);
	
}
