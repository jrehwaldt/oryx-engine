package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class EmptyBehaviour implements RoutingBehaviour {

    public List<ProcessInstance> execute(ProcessInstance instance) {
        return new LinkedList<ProcessInstance>();
    }

}
