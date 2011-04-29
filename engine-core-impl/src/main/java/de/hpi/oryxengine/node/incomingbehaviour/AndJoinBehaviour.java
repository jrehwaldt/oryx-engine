package de.hpi.oryxengine.node.incomingbehaviour;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class AndJoinBehaviour. Realizes the joining of more than one incoming path.
 */
public class AndJoinBehaviour extends AbstractIncomingBehaviour {

    @Override
    public List<Token> join(Token token) {
        ProcessInstanceContext context = token.getInstance().getContext();
        context.setWaitingExecution(token.getLastTakenTransition());
        return super.join(token);
    }

    @Override
    protected boolean joinable(Token token) {

        return token.joinable();
    }

    @Override
    protected List<Token> performJoin(Token token) {

        // We can do this, as we currently assume that an and join has a single outgoing transition
        List<Token> newTokens = new LinkedList<Token>();
        newTokens.add(token.performJoin());
        return newTokens;
    }

}
