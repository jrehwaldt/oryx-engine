package org.jodaengine.node.incomingbehaviour.petri;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenUtil;


/**
 * The Class TransitionJoinBehaviour. Consumes tokens and checks if it's possible to join.
 */
public class TransitionJoinBehaviour implements IncomingBehaviour {

    @Override
    // Just for info: The join is not directly needed, but the call of the method consumeTokens is important.
    public List<Token> join(Token token) {
        consumeTokens(token.getCurrentNode().getIncomingTransitions(), token.getInstance());
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(token);
        return tokens;
    }

    @Override
    public boolean joinable(Token token, Node node) {
        TokenUtil util = new TokenUtil();
        
        // attention: the node is not the current node of the token, it is a reachable node from the current node.
        for (Transition t : node.getIncomingTransitions()) {
            if (util.getTokensWhichAreOnPlace(t.getSource(), token.getInstance()).size() == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Consumes used tokens. 
     *
     * @param transitions the incoming transitions to get the source places
     * @param instance the instance
     */
    private void consumeTokens(List<Transition> transitions, AbstractProcessInstance instance) {

        TokenUtil util = new TokenUtil();
        List<Token> oldTokensOnPlace;
        for (Transition t : transitions) {
            Node placeBeforePetriTransition = t.getSource();
            // get Tokens, which are still there
            oldTokensOnPlace = util.getTokensWhichAreOnPlace(placeBeforePetriTransition, instance);
            
            // One token of the place should be deleted.
            // Because these are ordinary petri net's all tokens are equal and therefore we can delete just the first.
            if (oldTokensOnPlace.size() > 0) {
                //oldTokens.get(0).setCurrentNode(nextPetriTranisiton);
                instance.removeToken(oldTokensOnPlace.get(0));
            }
            
            // TODO Token auch aus dem Navigator löschen...das ist aber nicht so einfach wegen
            // Concurrency issues...hier muss beim herausnehmen miot Locks aquire/release gearbeitet werden
            
        }
    }

}
