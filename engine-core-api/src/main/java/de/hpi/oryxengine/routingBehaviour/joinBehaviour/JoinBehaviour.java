package de.hpi.oryxengine.routingBehaviour.joinBehaviour;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;

/**
 * The Interface JoinBehaviour.
 */
public interface JoinBehaviour {

    /**
     * Join.
     * 
     * @param instance
     *            the instance for which the join-algorithm is invoked.
     * @return the result of the joining. Usually this list contains one or zero ProcessInstances (Example: And-Join).
     */
    List<ProcessInstance> join(ProcessInstance instance);
}
