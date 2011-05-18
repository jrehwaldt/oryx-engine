package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;

/**
 * The Class TakeAllSplitBehaviour. Will signal the first outgoing transition, of which the condition evaluates to true.
 */
public class XORSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split according to the transitions.
     * 
     * @param instances
     *            the instances
     * @return the list
     * @throws NoValidPathException
     *             the no valid path exception {@inheritDoc}
     */
    @Override
    public List<Token> split(List<Token> instances)
    throws NoValidPathException {

        // TODO why do the instances get returned here, could somebody please leave a short comment on that? Didn't get
        // it right away, so probably somebody else won't too
        if (instances.size() == 0) {
            return instances;
        }

        List<Transition> transitionList = new ArrayList<Transition>();
        List<Token> transitionsToNavigate = null;

        // we look through the outgoing transitions and try to find one at least, whose condition evaluates true and
        // then return it as the to-be-taken transition
        // if there only is one outgoing one, take this one
        for (Token instance : instances) {
            Node currentNode = instance.getCurrentNode();
            List<Transition> outgoingTransitions = currentNode.getOutgoingTransitions();

            if (outgoingTransitions.size() == 1) {
                transitionList.add(outgoingTransitions.get(0));
            } else {
                for (Transition transition : currentNode.getOutgoingTransitions()) {
                    if (transition.getCondition().evaluate(instance)) {
                        transitionList.add(transition);
                        break;
                    }
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
