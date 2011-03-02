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
     * TODO Discuss communication issue with thorben and comment it
     */
    public List<ProcessInstance> split(List<ProcessInstance> instances) {

        if (instances.size() == 0) {
            return instances;
        }
        List<Node> nodeList = new ArrayList<Node>();
        List<ProcessInstance> instancesToNavigate = new ArrayList<ProcessInstance>();
        
        for (ProcessInstance instance : instances) {
            Node currentNode = instance.getCurrentNode();
            for (Transition transition : currentNode.getTransitions()) {
                nodeList.add(transition.getDestination());
            }

            try {
                instancesToNavigate.addAll(instance.navigateTo(nodeList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instancesToNavigate;
    }

}
