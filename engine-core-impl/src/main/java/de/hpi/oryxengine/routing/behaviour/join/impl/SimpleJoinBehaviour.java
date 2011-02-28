package de.hpi.oryxengine.routing.behaviour.join.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.joinBehaviour.JoinBehaviour;

/**
 * The Class SimpleJoinBehaviour. Just takes the incoming instance and performs no real joining at all.
 */
public class SimpleJoinBehaviour implements JoinBehaviour {

    /**
     * Join Behviour.
     *
     * @param instance the instance
     * @return the list of joined instances
     * @see de.hpi.oryxengine.joinBehaviour.JoinBehaviour#join(de.hpi.oryxengine.processInstance.ProcessInstance)
     */
    public List<ProcessInstance> join(ProcessInstance instance) {

        List<ProcessInstance> joinedInstances = new ArrayList<ProcessInstance>();
        joinedInstances.add(instance);
        return joinedInstances;
    }

}
