package de.hpi.oryxengine.node.incomingbehaviour;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.node.incomingbehaviour.IncomingBehaviour;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class AbstractJoinBehaviour.
 */
public abstract class AbstractIncomingBehaviour implements IncomingBehaviour {

    /**
     * Join.
     *
     * @param instance instance to perform the join on
     * @return the list of joined instances
     * @see de.hpi.oryxengine.node.incomingbehaviour.IncomingBehaviour
     * #join(de.hpi.oryxengine.process.token.Token) Do not override this, as it is a template method.
     * Only join, if a join is possible.
     */
    public List<Token> join(Token instance) {

        List<Token> instances = new LinkedList<Token>();
        if (joinable(instance)) {
            instances = performJoin(instance);
        }
        return instances;
    }

    /**
     * Joinable.
     * 
     * @param instance
     *            the instance that the join is triggered for.
     * @return true, if a join can be performed, e.g. for a BPMN AND-Join, all sibling-instances have to reached the
     *         join node as well.
     */
    protected abstract boolean joinable(Token instance);

    /**
     * Perform join.
     * 
     * @param instance
     *            the instance that the join is triggered for.
     * @return the result of the join.
     */
    protected abstract List<Token> performJoin(Token instance);
}
