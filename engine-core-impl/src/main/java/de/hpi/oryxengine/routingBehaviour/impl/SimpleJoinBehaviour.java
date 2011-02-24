package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.JoinBehaviour;

public class SimpleJoinBehaviour implements JoinBehaviour {

    public List<ProcessInstance> join(ProcessInstance instance) {

        List<ProcessInstance> joinedInstances = new ArrayList<ProcessInstance>();
        joinedInstances.add(instance);
        return joinedInstances;
    }

}
