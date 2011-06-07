package org.jodaengine.process.token;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;

/**
 * 
 * @author Jannik
 * 
 */

/**
 * The Class TokenUtil, which can be used for some useful token operations. However,
 *  not every modeling language may need these functions, so it was bundled here.
 */
public class TokenUtil {
    
    /**
     * Gets the tokens which are on the specific place.
     *
     * @param node the node
     * @param instance the instance
     * @return the tokens which are on the node
     */
    public List<Token> getTokensWhichAreOnNode(Node node, AbstractProcessInstance instance) {
        List<Token> tokensOnNode = new ArrayList<Token>();
        for (Token token : instance.getAssignedTokens()) {
            if (checkIfTokenIsOnNode(token, node)) {
                tokensOnNode.add(token);
            }
        }
        return tokensOnNode;

    }
    
    /**
     * Checks, if the token is on the given node.
     *
     * @param token the token
     * @param nodeBeforeControlFlow the node before {@link ControlFlow}
     * @return true, if successful
     */
    private boolean checkIfTokenIsOnNode(Token token, Node nodeBeforeControlFlow) {
        return token.getCurrentNode() == nodeBeforeControlFlow;
    }

}
