package org.jodaengine.node.petri.incomingbehaviour;

import java.util.LinkedList;
import java.util.List;

import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenImpl;

/**
 * The Class PetriIncomingBehaviour.
 */
public class TransitionIncomingBehaviour extends AbstractPetriIncomingBehaviour implements IncomingBehaviour {

    @Override
    public List<Token> join(Token token) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean joinable(Token token) {

        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected List<Token> performJoin(Token token) {

        // Produce a new token
        List<Token> newTokens = new LinkedList<Token>();
        Token producedToken = new TokenImpl(token.getCurrentNode(), token.getInstance());
        newTokens.add(producedToken);
        
        //Delete the old ones
        token.getInstance().removeToken(token);
        //TODO: Also the other ones, which are located before the enabled transition
        
        // Delete the enabled marks
        token.performJoin();
        return newTokens;
    }

}
