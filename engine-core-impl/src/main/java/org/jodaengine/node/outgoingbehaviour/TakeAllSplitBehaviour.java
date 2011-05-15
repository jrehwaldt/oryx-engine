package org.jodaengine.node.outgoingbehaviour;

import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class TakeAllSplitBehaviour. Will signalize all outgoing transitions.
 */
public class TakeAllSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split Behaviour.
     * It takes all transitions so a classic AND-Split behaviour.
     *
     * @param tokens the instances
     * @return the list
     * @see de.hpi.oryxengine.OutgoingBehaviour.SplitBehaviour#split(java.util.List)
     */
    @Override
    public List<Token> split(List<Token> tokens) {

        if (tokens == null || tokens.size() == 0) {
            return tokens;
        }
        List<Token> tokensToNavigate = new ArrayList<Token>();
        
        for (Token token : tokens) {
            Node currentNode = token.getCurrentNode();

            try {
                tokensToNavigate.addAll(token.navigateTo(currentNode.getOutgoingTransitions()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tokensToNavigate;
    }

}
