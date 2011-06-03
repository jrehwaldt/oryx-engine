package org.jodaengine.process.token;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;

public class TokenUtil {
    
    public List<Token> getTokensWhichAreOnPlace(Node placeBeforeTransition, AbstractProcessInstance instance) {
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
