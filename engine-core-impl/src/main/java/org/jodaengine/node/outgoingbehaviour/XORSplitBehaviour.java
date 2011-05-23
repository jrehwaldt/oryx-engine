package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.BPMNToken;

/**
 * The Class TakeAllSplitBehaviour. Will signal the first outgoing transition, of which the condition evaluates to true.
 */
public class XORSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split according to the transitions.
     *
     * @param bPMNTokens the b pmn tokens
     * @return the list
     * @throws NoValidPathException the no valid path exception {@inheritDoc}
     */
    @Override
    public List<BPMNToken> split(List<BPMNToken> bPMNTokens)
    throws NoValidPathException {

        if (bPMNTokens.size() == 0) {
            return bPMNTokens;
        }

        List<Transition<BPMNToken>> transitionList = new ArrayList<Transition<BPMNToken>>();
        List<BPMNToken> transitionsToNavigate = null;

        // we look through the outgoing transitions and try to find one at least, whose condition evaluates true and
        // then return it as the to-be-taken transition
        for (BPMNToken bPMNToken : bPMNTokens) {
            Node<BPMNToken> currentNode = bPMNToken.getCurrentNode();
            for (Transition<BPMNToken> transition : currentNode.getOutgoingTransitions()) {
                if (transition.getCondition().evaluate(bPMNToken)) {
                    transitionList.add(transition);
                    break;
                }
            }

            if (transitionList.size() == 0) {

                throw new NoValidPathException();

            }

            transitionsToNavigate = bPMNToken.navigateTo(transitionList);

        }
        return transitionsToNavigate;
    }

}
