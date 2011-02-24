package de.hpi.oryxengine.routingBehaviour;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class EmptyRoutingBehaviour implements RoutingBehaviour {

    public List<ProcessInstance> execute(ProcessInstance instance) {

        return new LinkedList<ProcessInstance>();
    }

}
