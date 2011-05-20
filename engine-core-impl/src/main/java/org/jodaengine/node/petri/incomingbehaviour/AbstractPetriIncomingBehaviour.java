package org.jodaengine.node.petri.incomingbehaviour;

import java.util.LinkedList;
import java.util.List;

import org.jodaengine.node.incomingbehaviour.AbstractIncomingBehaviour;
import org.jodaengine.process.token.Token;


/**
 * The Class AbstractPetriIncomingBehaviour. Needs to extend AbstractIncomingBehaviour, because tokens get removed there.
 */
public abstract class AbstractPetriIncomingBehaviour extends AbstractIncomingBehaviour {

    
    @Override
    public List<Token> join(Token token) {

        List<Token> tokens = new LinkedList<Token>();
        if (joinable(token)) {
            tokens = performJoin(token);
        }
        return tokens;
    }

}
