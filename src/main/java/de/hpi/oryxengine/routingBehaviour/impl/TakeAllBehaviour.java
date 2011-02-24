package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class TakeAllBehaviour implements RoutingBehaviour {

    public TakeAllBehaviour() {

    }

    public List<ProcessInstance> execute(ProcessInstance instance) {

        return instance.takeAllTransitions();
    }

}
