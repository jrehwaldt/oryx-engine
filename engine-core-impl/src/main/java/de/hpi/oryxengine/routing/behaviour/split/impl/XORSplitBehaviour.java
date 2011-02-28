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
public class XORSplitBehaviour implements SplitBehaviour {

    /**
     * Split Behaviour.
     * It takes exactly one transition, if mutliple transitions are true the first one is taken
     *
     * @param list of process instances
     * @return the list of to navigate process instances.
     * @see de.hpi.oryxengine.splitBehaviour.SplitBehaviour#split(java.util.List)
     */
    public List<ProcessInstance> split(List<ProcessInstance> instances) {

        if (instances.size() == 0) {
            return instances;
        }
        List<Node> nodeList = new ArrayList<Node>();
        List<ProcessInstance> instancesToNavigate = null;
        
        for (ProcessInstance instance : instances) {
            Node currentNode = instance.getCurrentNode();
            for (Transition transition : currentNode.getTransitions()) {
                if (transition.getCondition().evaluate()) {
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
