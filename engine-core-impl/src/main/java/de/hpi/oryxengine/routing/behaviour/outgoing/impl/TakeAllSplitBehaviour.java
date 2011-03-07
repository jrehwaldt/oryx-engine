package de.hpi.oryxengine.routing.behaviour.outgoing.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class TakeAllSplitBehaviour. Will signalize all outgoing transitions.
 */
public class TakeAllSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split Behaviour.
     * It takes all transitions so a classic AND-Split behaviour.
     *
     * @param instances the instances
     * @return the list
     * @see de.hpi.oryxengine.OutgoingBehaviour.SplitBehaviour#split(java.util.List)
     * TODO Discuss communication issue with thorben and comment it
     */
    public List<Token> split(List<Token> instances) {

        if (instances.size() == 0) {
            return instances;
        }
        List<Node> nodeList = new ArrayList<Node>();
        List<Token> instancesToNavigate = new ArrayList<Token>();
        
        for (Token instance : instances) {
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
