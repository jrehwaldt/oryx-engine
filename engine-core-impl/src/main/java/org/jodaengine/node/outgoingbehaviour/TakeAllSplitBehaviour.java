package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;


/**
 * The Class TakeAllSplitBehaviour. Will signalize all outgoing {@link ControlFlow}s.
 */
public class TakeAllSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split Behaviour.
     * It takes all {@link ControlFlow}s so a classic AND-Split behaviour.
     *
     * @param tokens the instances
     * @return the list
     * @see org.jodaengine.OutgoingBehaviour.SplitBehaviour#split(java.util.List)
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
                tokensToNavigate.addAll(token.navigateTo(currentNode.getOutgoingControlFlows()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tokensToNavigate;
    }

}
