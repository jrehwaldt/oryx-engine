package de.hpi.oryxengine.routingBehaviour.impl;

import de.hpi.oryxengine.joinBehaviour.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routingBehaviour.AbstractRoutingBehaviour;
import de.hpi.oryxengine.splitBehaviour.impl.TakeAllSplitBehaviour;

public class TakeAllBehaviour extends AbstractRoutingBehaviour {

    public TakeAllBehaviour() {

        this.joinBehaviour = new SimpleJoinBehaviour();
        this.splitBehaviour = new TakeAllSplitBehaviour();
    }
}
