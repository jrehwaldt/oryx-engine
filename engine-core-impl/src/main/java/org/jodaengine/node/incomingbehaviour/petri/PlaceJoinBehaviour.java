package org.jodaengine.node.incomingbehaviour.petri;

import java.util.LinkedList;
import java.util.List;

import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.Token;


/**
 * The Class AndJoinBehaviour. Realizes the joining of more than one incoming path.
 */
public class PlaceJoinBehaviour implements IncomingBehaviour {

    @Override
    public List<Token> join(Token token) {
        ProcessInstanceContext context = token.getInstance().getContext();
        
        // We have an AND Join, so we have to wait untill all paths are ready.
        // The following line states, that the path with the current token is ready.
        context.setWaitingExecution(token.getLastTakenTransition());
        return super.join(token);
    }

    @Override
    protected boolean joinable(Token token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        return context.allIncomingTransitionsSignaled(token.getCurrentNode());
    }

    @Override
    protected List<Token> performJoin(Token token) {

        // We can do this, as we currently assume that an and join has a single outgoing transition
        List<Token> newTokens = new LinkedList<Token>();
        
        ProcessInstanceContext context = token.getInstance().getContext();
        context.removeIncomingTransitions(token.getCurrentNode());
        newTokens.add(token);
        return newTokens;
    }

}
