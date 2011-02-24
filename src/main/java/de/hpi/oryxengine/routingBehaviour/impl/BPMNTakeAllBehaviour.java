package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class BPMNTakeAllBehaviour implements RoutingBehaviour {

    public BPMNTakeAllBehaviour() {

    }

    public List<ProcessInstance> execute(ProcessInstance instance) {

        List<ProcessInstance> instancesToNavigate = new LinkedList<ProcessInstance>();
        ArrayList<Transition> transitions = instance.getCurrentNode().getTransitions();
        if (transitions.size() == 1) {
            Transition transition = transitions.get(0);
            NodeImpl destination = transition.getDestination();
            instance.setCurrentNode(destination);
            instancesToNavigate.add(instance);
        } else {
            for (Transition transition : transitions) {
                // Create new child instances etc.
            }
        }
        return instancesToNavigate;
    }

}
