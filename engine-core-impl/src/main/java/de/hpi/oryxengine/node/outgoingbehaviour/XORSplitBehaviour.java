package de.hpi.oryxengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.exception.NoValidPathException;
import de.hpi.oryxengine.node.outgoingbehaviour.OutgoingBehaviour;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class TakeAllSplitBehaviour. Will signal all outgoing transitions.
 */
public class XORSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split.
     *
     * @param instances the instances
     * @return the list
     * @throws NoValidPathException the no valid path exception
     * {@inheritDoc}
     */
    @Override
    public List<Token> split(List<Token> instances) throws NoValidPathException {

        if (instances.size() == 0) {
            return instances;
        }
        List<Transition> transitionList = new ArrayList<Transition>();
        List<Token> transitionsToNavigate = null;
        
        for (Token instance : instances) {
            Node currentNode = instance.getCurrentNode();
            for (Transition transition : currentNode.getOutgoingTransitions()) {
                if (transition.getCondition().evaluate(instance)) {
                    transitionList.add(transition);
                    break;
                }
            }
            
            if (transitionList.size() == 0) {
                
                throw new NoValidPathException();
                
            }

            transitionsToNavigate = instance.navigateTo(transitionList);

        }
        return transitionsToNavigate;
    }

}
