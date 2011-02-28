package de.hpi.oryxengine.routing.behaviour;

import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.routing.behaviour.join.JoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.SplitBehaviour;

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
     * de.hpi.oryxengine.routing.behaviour.RoutingBehaviour#execute(de.hpi.oryxengine.process.instance.ProcessInstance)
     */
    public List<ProcessInstance> execute(ProcessInstance instance) {

        List<ProcessInstance> joinedInstances = joinBehaviour.join(instance);
        return splitBehaviour.split(joinedInstances);
    }
}
