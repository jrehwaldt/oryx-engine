package de.hpi.oryxengine.routing.behaviour.outgoing.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class TakeAllSplitBehaviour. Will signal all outgoing transitions.
 */
public class XORSplitBehaviour implements OutgoingBehaviour {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProcessInstance> split(List<ProcessInstance> instances) {

        if (instances.size() == 0) {
            return instances;
        }
        List<Node> nodeList = new ArrayList<Node>();
        List<ProcessInstance> instancesToNavigate = null;
        
        for (ProcessInstance instance : instances) {
            Node currentNode = instance.getCurrentNode();
            for (Transition transition : currentNode.getTransitions()) {
                if (transition.getCondition().evaluate(instance)) {
                    nodeList.add(transition.getDestination());
                    break;
                }
            }

            try {
                instancesToNavigate = instance.navigateTo(nodeList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instancesToNavigate;
    }

}
