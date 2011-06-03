package org.jodaengine.node.incomingbehaviour.petri;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;


/**
 * The Class AndJoinBehaviour. Realizes the joining of more than one incoming path.
 */
public class TransitionJoinBehaviour implements IncomingBehaviour {

    @Override
    public List<Token> join(Token token) {
        ProcessInstanceContext context = token.getInstance().getContext();
        
        // We have an AND Join, so we have to wait untill all paths are ready.
        // The following line states, that the path with the current token is ready.
        context.setWaitingExecution(token.getLastTakenTransition());
        return null;
    }

    @Override
    public boolean joinable(Token token, Node node) {

        //ProcessInstanceContext context = token.getInstance().getContext();
        //TODO Warum kennt das Behavior die zugehörige node nicht?
        //return context.allIncomingTransitionsSignaled(node);
        
        //node ist die nächste...
        boolean check = true;
        for(Transition t : node.getIncomingTransitions()) {
            if(getTokensWhichAreOnPlace(t.getSource(), token.getInstance()).size() == 0) {
                check = false;
                break;
            }
        }
        return check;
    }
    
    private List<Token> getTokensWhichAreOnPlace(Node placeBeforeTransition, AbstractProcessInstance instance) {
        List<Token> tokensOnPlace = new ArrayList<Token>();
        for(Token token : instance.getAssignedTokens()) {
            if(checkIfTokenIsOnPlace(token, placeBeforeTransition)) {
                tokensOnPlace.add(token);
            }
        }
        return tokensOnPlace;

    }
    
    private boolean checkIfTokenIsOnPlace(Token token, Node placeBeforeTransition) {
        return token.getCurrentNode() == placeBeforeTransition;
    }

}
