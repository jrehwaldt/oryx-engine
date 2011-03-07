package de.hpi.oryxengine.navigator;

import java.util.UUID;

/**
 * The Interface NavigatorInterface.
 */
public interface Navigator {

    /**
     * Start a new process instance.
     * 
     * @param processID
     *            the id of the process that is to be instantiated
     * @return the id of the newly created process instance
     */
    String startProcessInstance(UUID processID);

    /**
     * Stop process instance.
     * 
     * @param instanceID
     *            the id of the instance that is to be stopped
     */
    void stopProcessInstance(UUID instanceID);

    /**
     * Gets the current instance state.
     *
     * @param instanceID the processinstance id
     * @return the current instance state
     */
    String getCurrentInstanceState(UUID instanceID);

}
