package de.hpi.oryxengine.routingBehaviour;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.joinBehaviour.JoinBehaviour;
import de.hpi.oryxengine.routingBehaviour.splitBehaviour.SplitBehaviour;

/**
 * The Class AbstractRoutingBehaviour.
 */
public abstract class AbstractRoutingBehaviour implements RoutingBehaviour {

    /** The join behaviour. */
    protected JoinBehaviour joinBehaviour;

    /** The split behaviour. */
    protected SplitBehaviour splitBehaviour;

    /**
     * Behaves like the routing behaviour should behave. That is joining and splitting as defined.
     * 
     * @param instance
     *            the instance
     * @return the list
     * @see 
     * de.hpi.oryxengine.routingBehaviour.RoutingBehaviour#execute(de.hpi.oryxengine.processInstance.ProcessInstance)
     */
    public List<ProcessInstance> execute(ProcessInstance instance) {

        List<ProcessInstance> joinedInstances = joinBehaviour.join(instance);
        return splitBehaviour.split(joinedInstances);
    }
}
