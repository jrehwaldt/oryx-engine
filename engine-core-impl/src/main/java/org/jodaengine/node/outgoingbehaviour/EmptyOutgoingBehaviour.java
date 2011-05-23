package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.process.token.BPMNToken;


/**
 * The Class EmptyOutgoingBehaviour. This is needed for {@link BpmnEndActivity}s as they don't have outgoing edges and
 * need
 * to handle this.
 */
public class EmptyOutgoingBehaviour implements OutgoingBehaviour {

    @Override
    public List<BPMNToken> split(List<BPMNToken> bPMNTokens) {
        return new ArrayList<BPMNToken>();


    }

}
