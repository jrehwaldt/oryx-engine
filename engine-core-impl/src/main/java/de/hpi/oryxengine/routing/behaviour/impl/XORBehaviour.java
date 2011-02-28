package de.hpi.oryxengine.routing.behaviour.impl;

import de.hpi.oryxengine.routing.behaviour.AbstractRoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.join.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.impl.XORSplitBehaviour;

/**
 * The Class AndJoinGatewayBehaviour.
 */
public class XORBehaviour extends AbstractRoutingBehaviour {

    /**
     * Instantiates a new and join gateway behaviour. It consists of the andJoinBehaviour and signals all outgoing
     * transitions, which is usually only one.
     */
    public XORBehaviour() {

        this.joinBehaviour = new SimpleJoinBehaviour();
        this.splitBehaviour = new XORSplitBehaviour();
    }
}
