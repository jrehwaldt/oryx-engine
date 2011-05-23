package org.jodaengine.node.petri.incomingbehaviour;

import java.util.LinkedList;
import java.util.List;

import org.jodaengine.node.incomingbehaviour.AbstractIncomingBehaviour;
import org.jodaengine.process.token.BPMNToken;


/**
 * The Class AbstractPetriIncomingBehaviour. Needs to extend AbstractIncomingBehaviour, because tokens get removed there.
 */
public abstract class AbstractPetriIncomingBehaviour extends AbstractIncomingBehaviour {

    
    @Override
    public List<BPMNToken> join(BPMNToken bPMNToken) {

        List<BPMNToken> bPMNTokens = new LinkedList<BPMNToken>();
        if (joinable(bPMNToken)) {
            bPMNTokens = performJoin(bPMNToken);
        }
        return bPMNTokens;
    }

}
