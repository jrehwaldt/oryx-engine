package de.hpi.oryxengine.routing.behaviour.incoming.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;

/**
 * The Class SimpleJoinBehaviour. Just takes the incoming instance and performs no real joining at all.
 */
public class SimpleJoinBehaviour implements IncomingBehaviour {

    /**
     * Join Behviour.
     *
     * @param instance the instance
     * @return the list of joined instances
     * @see de.hpi.oryxengine.IncomingBehaviour.JoinBehaviour#join(de.hpi.oryxengine.process.instance.ProcessInstance)
     */
    public List<ProcessInstance> join(ProcessInstance instance) {

        List<ProcessInstance> joinedInstances = new ArrayList<ProcessInstance>();
        joinedInstances.add(instance);
        return joinedInstances;
    }

}
