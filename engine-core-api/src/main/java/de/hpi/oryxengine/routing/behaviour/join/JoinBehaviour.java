package de.hpi.oryxengine.routing.behaviour.join;

import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;

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
