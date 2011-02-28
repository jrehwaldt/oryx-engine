package de.hpi.oryxengine.routing.behaviour.impl;

import de.hpi.oryxengine.routing.behaviour.AbstractRoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.join.impl.AndJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.impl.TakeAllSplitBehaviour;

/**
 * The Class AndJoinGatewayBehaviour.
 */
public class AndJoinGatewayBehaviour extends AbstractRoutingBehaviour {

    /**
     * Instantiates a new and join gateway behaviour. It consists of the andJoinBehaviour and signals all outgoing
     * transitions, which is usually only one.
     */
    public AndJoinGatewayBehaviour() {

        this.joinBehaviour = new AndJoinBehaviour();
        this.splitBehaviour = new TakeAllSplitBehaviour();
    }
}
