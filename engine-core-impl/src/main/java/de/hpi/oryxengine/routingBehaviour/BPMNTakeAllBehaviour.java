package de.hpi.oryxengine.routingBehaviour;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.NodeImpl;
import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class BPMNTakeAllBehaviour implements RoutingBehaviour {

    public BPMNTakeAllBehaviour() {

    }

    public List<ProcessInstance> execute(ProcessInstance instance) {

        return instance.takeAllTransitions();
    }

}
