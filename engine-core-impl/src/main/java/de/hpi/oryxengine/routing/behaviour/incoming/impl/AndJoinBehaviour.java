package de.hpi.oryxengine.routing.behaviour.incoming.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.AbstractIncomingBehaviour;

/**
 * The Class AndJoinBehaviour. Realizes the joining of more than one incoming path.
 */
public class AndJoinBehaviour extends AbstractIncomingBehaviour {

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.routingBehaviour.joinBehaviour.AbstractJoinBehaviour#joinable(de.hpi.oryxengine.processInstance
     * .ProcessInstance)
     */
    @Override
    protected boolean joinable(Token instance) {

        Node currentNode = instance.getCurrentNode();
        Token parent = instance.getParentToken();
        boolean joinable = true;

        List<Token> childInstances = parent.getChildTokens();
        for (Token child : childInstances) {
            if (child.getCurrentNode() != currentNode) {
                joinable = false;
            }
        }

        return joinable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.routingBehaviour.joinBehaviour.AbstractJoinBehaviour
     *      #performJoin(de.hpi.oryxengine.processInstance
     * .ProcessInstance)
     */
    @Override
    protected List<Token> performJoin(Token instance) {

        // We can do this, as we currently assume that an and join has a single outgoing transition
        List<Token> newInstances = new LinkedList<Token>();
        Node currentNode = instance.getCurrentNode();
        Token parent = instance.getParentToken();
        parent.setCurrentNode(currentNode);
        newInstances.add(parent);
        return newInstances;
    }

}
