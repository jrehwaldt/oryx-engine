package org.jodaengine.node.incomingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.process.token.BPMNToken;


/**
 * The Class SimpleJoinBehaviour. Just takes the incoming instance and performs no real joining at all.
 */
public class SimpleJoinBehaviour implements IncomingBehaviour<BPMNToken> {

    @Override
    public List<BPMNToken> join(BPMNToken bPMNToken) {

        List<BPMNToken> joinedInstances = new ArrayList<BPMNToken>();
        joinedInstances.add(bPMNToken);
        return joinedInstances;
    }

}
