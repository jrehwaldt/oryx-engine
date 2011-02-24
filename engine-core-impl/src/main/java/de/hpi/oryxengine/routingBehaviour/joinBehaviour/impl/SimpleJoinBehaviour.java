package de.hpi.oryxengine.routingBehaviour.joinBehaviour.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.joinBehaviour.JoinBehaviour;

/**
 * The Class SimpleJoinBehaviour. Just takes the incoming instance and performs no real joining at all.
 */
public class SimpleJoinBehaviour implements JoinBehaviour {

    /* (non-Javadoc)
     * @see de.hpi.oryxengine.joinBehaviour.JoinBehaviour#join(de.hpi.oryxengine.processInstance.ProcessInstance)
     */
    public List<ProcessInstance> join(ProcessInstance instance) {

        List<ProcessInstance> joinedInstances = new ArrayList<ProcessInstance>();
        joinedInstances.add(instance);
        return joinedInstances;
    }

}
