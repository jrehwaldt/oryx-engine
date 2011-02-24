package de.hpi.oryxengine.routingBehaviour.impl;

import de.hpi.oryxengine.routingBehaviour.AbstractRoutingBehaviour;
import de.hpi.oryxengine.routingBehaviour.joinBehaviour.impl.AndJoinBehaviour;
import de.hpi.oryxengine.routingBehaviour.splitBehaviour.impl.TakeAllSplitBehaviour;

public class AndJoinGatewayBehaviour extends AbstractRoutingBehaviour {

    public AndJoinGatewayBehaviour() {

        this.joinBehaviour = new AndJoinBehaviour();
        this.splitBehaviour = new TakeAllSplitBehaviour();
    }
}
