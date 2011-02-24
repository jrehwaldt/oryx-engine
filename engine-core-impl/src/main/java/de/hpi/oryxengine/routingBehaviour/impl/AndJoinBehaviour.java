package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.routingBehaviour.AbstractJoinBehaviour;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class AndJoinBehaviour extends AbstractJoinBehaviour {

    @Override
    protected boolean joinable(ProcessInstance instance) {

        Node currentNode = instance.getCurrentNode();
        ProcessInstance parent = instance.getParentInstance();
        boolean joinable = true;

        List<ProcessInstance> childInstances = parent.getChildInstances();
        for (ProcessInstance child : childInstances) {
            if (child.getCurrentNode() != currentNode) {
                joinable = false;
            }
        }

        return joinable;
    }

    @Override
    protected List<ProcessInstance> performJoin(ProcessInstance instance) {

        // We can do this, as we currently assume that an and join has a single outgoing transition
        List<ProcessInstance> newInstances = new LinkedList<ProcessInstance>();
        Node currentNode = instance.getCurrentNode();
        ProcessInstance parent = instance.getParentInstance();
        parent.setCurrentNode(currentNode);
        newInstances.add(parent);
        return newInstances;
    }

}
