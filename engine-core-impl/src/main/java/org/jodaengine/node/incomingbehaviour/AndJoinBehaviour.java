package org.jodaengine.node.incomingbehaviour;

import java.util.Collection;
import java.util.LinkedList;

import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * The class AndJoinBehaviour realizes the joining of more than one incoming path.
 */
public class AndJoinBehaviour extends AbstractIncomingBehaviour {
    
    @Override
    public Collection<Token> join(Token token) {
        ProcessInstanceContext context = token.getInstance().getContext();
        
        // We have an AND Join, so we have to wait until all paths are ready.
        // The following line states, that the path with the current token is ready.
        context.setSignaledControlFlow(token.getLastTakenControlFlow());
        return super.join(token);
    }

    @Override
    public boolean joinable(Token token, Node node) {

        ProcessInstanceContext context = token.getInstance().getContext();
        return context.allIncomingControlFlowsSignaled(node);
    }

    @Override
    protected Collection<Token> performJoin(Token token) {

        // We can do this, as we currently assume that an and join has a single outgoing {@link ControlFlow}
        Collection<Token> newTokens = new LinkedList<Token>();
        
        ProcessInstanceContext context = token.getInstance().getContext();
        context.removeSignaledControlFlows(token.getCurrentNode());
        newTokens.add(token);
        return newTokens;
    }

}
