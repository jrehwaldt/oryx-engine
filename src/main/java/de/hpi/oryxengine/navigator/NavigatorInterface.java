package de.hpi.oryxengine.navigator;

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
	public int startProcessInstance(int processID);
	
	/**
	 * Stop process instance.
	 *
	 * @param instanceID the id of the instance that is to be stopped
	 */
	public void stopProcessInstance(int instanceID);
	
	public String getCurrentInstanceState(int instanceID);
	
	
}
