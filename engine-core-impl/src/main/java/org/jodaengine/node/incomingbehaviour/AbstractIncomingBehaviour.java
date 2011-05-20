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
        if (joinable(token)) {
            tokens = performJoin(token);
        } else {
            // remove the token because we don't need it later on
            token.getInstance().removeToken(token);
        }
        return tokens;
    }

    /**
     * Joinable.
     *
     * @param token the token
     * @return true, if a join can be performed, e.g. for a BPMN AND-Join, all sibling-instances have to reached the
     * join node as well.
     */
    protected abstract boolean joinable(Token token);

    /**
     * Perform join.
     *
     * @param token the token
     * @return the result of the join.
     */
    protected abstract List<Token> performJoin(Token token);
}
