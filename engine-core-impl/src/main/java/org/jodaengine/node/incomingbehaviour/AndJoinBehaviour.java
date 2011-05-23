package org.jodaengine.node.incomingbehaviour;

import java.util.LinkedList;
import java.util.List;

import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.BPMNToken;


/**
 * The Class AndJoinBehaviour. Realizes the joining of more than one incoming path.
 */
public class AndJoinBehaviour extends AbstractIncomingBehaviour {

    @Override
    public List<BPMNToken> join(BPMNToken token) {
        ProcessInstanceContext context = token.getInstance().getContext();
        context.setWaitingExecution(token.getLastTakenTransition());
        return super.join(token);
    }

    @Override
    protected boolean joinable(BPMNToken token) {

        return token.joinable();
    }

    @Override
    protected List<BPMNToken> performJoin(BPMNToken token) {

        // We can do this, as we currently assume that an and join has a single outgoing transition
        List<BPMNToken> newTokens = new LinkedList<BPMNToken>();
        newTokens.add(token.performJoin());
        return newTokens;
    }

}
