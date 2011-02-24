package de.hpi.oryxengine.routingBehaviour.impl;

import de.hpi.oryxengine.routingBehaviour.AbstractRoutingBehaviour;

public class AndJoinGatewayBehaviour extends AbstractRoutingBehaviour {

    public AndJoinGatewayBehaviour() {

        this.joinBehaviour = new AndJoinBehaviour();
        this.splitBehaviour = new TakeAllSplitBehaviour();
    }
}
