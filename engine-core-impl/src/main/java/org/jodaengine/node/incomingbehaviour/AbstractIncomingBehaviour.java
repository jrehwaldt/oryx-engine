package org.jodaengine.node.incomingbehaviour;

import java.util.LinkedList;
import java.util.List;

import org.jodaengine.process.token.Token;



/**
 * The Class AbstractJoinBehaviour.
 */
public abstract class AbstractIncomingBehaviour implements IncomingBehaviour {

    /**
     * Join.
     *
     * @param token instance to perform the join on
     * @return the list of joined instances
     * @see org.jodaengine.node.incomingbehaviour.IncomingBehaviour
     * #join(org.jodaengine.process.token.Token) Do not override this, as it is a template method.
     * Only join, if a join is possible.
     */
    public List<Token> join(Token token) {

        List<Token> tokens = new LinkedList<Token>();
        
        // Are all required paths there?
        if (joinable(token, token.getCurrentNode())) {
            // then lets do the join
            tokens = performJoin(token);
        } else {
            // remove the token because we don't need it later on. The path was already signaled.
            token.getInstance().removeToken(token);
        }
        return tokens;
    }


    /**
     * Perform join.
     * 
     * @param instance
     *            the instance that the join is triggered for.
     * @return the result of the join.
     */
    protected abstract List<Token> performJoin(Token token);
}
