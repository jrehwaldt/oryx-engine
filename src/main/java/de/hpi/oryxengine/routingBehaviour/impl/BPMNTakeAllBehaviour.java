package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class BPMNTakeAllBehaviour implements RoutingBehaviour {

    public BPMNTakeAllBehaviour() {

    }

    public List<ProcessInstance> execute(ProcessInstance instance) {

        return instance.takeAllTransitions();
    }

}
