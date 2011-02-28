package de.hpi.oryxengine.routing.behaviour.split.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.routing.behaviour.split.SplitBehaviour;

/**
 * The Class TakeAllSplitBehaviour. Will signalize all outgoing transitions.
 */
public class TakeAllSplitBehaviour implements SplitBehaviour {

    /**
     * Split Behaviour.
     * It takes all transitions so a classic AND-Split behaviour.
     *
     * @param instances the instances
     * @return the list
     * @see de.hpi.oryxengine.splitBehaviour.SplitBehaviour#split(java.util.List)
     * TODO Discuss communication issue with thorben
     */
    public List<ProcessInstance> split(List<ProcessInstance> instances) {

        if (instances.size() == 0) {
            return instances;
        }
        List<Node> nodeList = new ArrayList<Node>();
        Node currentNode = instances.get(0).getCurrentNode();
        for(Transition transition : currentNode.getTransitions()) {
            nodeList.add(transition.getDestination());
        }
        List<ProcessInstance> instancesToNavigate = null;
        try {
            instancesToNavigate = instances.get(0).navigateTo(nodeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instancesToNavigate;
    }

}
