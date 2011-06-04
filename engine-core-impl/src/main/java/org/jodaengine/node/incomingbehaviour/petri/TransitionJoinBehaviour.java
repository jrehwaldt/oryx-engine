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
 * The Class AndJoinBehaviour. Realizes the joining of more than one incoming path.
 */
public class TransitionJoinBehaviour implements IncomingBehaviour {

    @Override
    public List<Token> join(Token token) {
        consumeTokens(token.getCurrentNode().getIncomingTransitions(), token.getInstance());
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(token);
        return tokens;
    }

    @Override
    public boolean joinable(Token token, Node node) {
        TokenUtil util = new TokenUtil();
        
        //node ist die nächste...
        boolean check = true;
        for (Transition t : node.getIncomingTransitions()) {
            if (util.getTokensWhichAreOnPlace(t.getSource(), token.getInstance()).size() == 0) {
                check = false;
                break;
            }
        }
        return check;
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
            
            //TODO Token auch aus dem Navigator löschen...das ist aber nicht so einfach wegen
            // Cuncurrency issues...hier muss beim herausnehmen miot Locks aquire/release gearbeitet werden
            
        }
    }

}
