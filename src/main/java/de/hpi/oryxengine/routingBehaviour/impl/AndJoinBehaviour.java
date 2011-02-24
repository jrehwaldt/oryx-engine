package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class AndJoinBehaviour implements RoutingBehaviour {

    public List<ProcessInstance> execute(ProcessInstance instance) {

        Node currentNode = instance.getCurrentNode();
        List<ProcessInstance> newInstances = new LinkedList<ProcessInstance>();
        ProcessInstance parent = instance.getParentInstance();
        boolean joinable = true;

        List<ProcessInstance> childInstances = parent.getChildInstances();
        for (ProcessInstance child : childInstances) {
            if (child.getCurrentNode() != currentNode) {
                joinable = false;
            }
        }

        if (!joinable) {
            return newInstances;
        } else {
            // We can do this, as we currently assume that an and join has a single outgoing transition
            Transition outgoingTransition = currentNode.getTransitions().get(0);
            Node followingNode = outgoingTransition.getDestination();
            parent.setCurrentNode(followingNode);
            newInstances.add(parent);
            return newInstances;
        }

    }

}
