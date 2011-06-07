package org.jodaengine.node.outgoingbehaviour.petri;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * The Class TransitionSplitBehaviour. It creates for every following place one new token.
 */
public class TransitionSplitBehaviour implements OutgoingBehaviour {

    @Override
    public List<Token> split(List<Token> tokens)
    throws NoValidPathException {
        
        AbstractProcessInstance instance;

        if (tokens == null || tokens.size() == 0) {
            return tokens;
        }
        List<Token> tokensToNavigate = new ArrayList<Token>();
        
        for (Token token : tokens) {
            instance = token.getInstance();
            Node currentNode = token.getCurrentNode();

            try {
                tokensToNavigate.addAll(token.navigateTo(currentNode.getOutgoingTransitions()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            instance.removeToken(token);
        }
        return tokensToNavigate;
    }

}
