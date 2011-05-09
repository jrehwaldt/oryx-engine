package de.hpi.oryxengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.NoValidPathException;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class TakeAllSplitBehaviour. Will signal the first outgoing transition, of which the condition evaluates to true.
 */
public class XORSplitBehaviour implements OutgoingBehaviour {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Split according to the transitions.
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
        logger.debug("XOR Transitionlist: " + transitionList.toString());
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
