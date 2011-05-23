package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;


/**
 * The Class TakeAllSplitBehaviour. Will signalize all outgoing transitions.
 */
public class TakeAllSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split Behaviour.
     * It takes all transitions so a classic AND-Split behaviour.
     *
     * @param tokens the tokens
     * @return the list
     * @see org.jodaengine.OutgoingBehaviour.SplitBehaviour#split(java.util.List)
     */
    @Override
    public List<BPMNToken> split(List<BPMNToken> tokens) {

        if (tokens == null || tokens.size() == 0) {
            return tokens;
        }
        List<BPMNToken> tokensToNavigate = new ArrayList<BPMNToken>();
        
        for (BPMNToken token : tokens) {
            Node<BPMNToken> currentNode = token.getCurrentNode();

            try {
                tokensToNavigate.addAll(token.navigateTo(currentNode.getOutgoingTransitions()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tokensToNavigate;
    }

}
