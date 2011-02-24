package de.hpi.oryxengine.routingBehaviour.joinBehaviour.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.joinBehaviour.JoinBehaviour;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class SimpleJoinBehaviour implements JoinBehaviour {

    public List<ProcessInstance> join(ProcessInstance instance) {

        List<ProcessInstance> joinedInstances = new ArrayList<ProcessInstance>();
        joinedInstances.add(instance);
        return joinedInstances;
    }

}
