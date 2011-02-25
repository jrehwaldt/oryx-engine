package de.hpi.oryxengine.routingBehaviour.impl;

import de.hpi.oryxengine.routingBehaviour.AbstractRoutingBehaviour;
import de.hpi.oryxengine.routingBehaviour.joinBehaviour.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routingBehaviour.splitBehaviour.impl.TakeAllSplitBehaviour;

/**
 * The Class TakeAllBehaviour. Does not join at all and signals all outgoing transitions.
 */
public class TakeAllBehaviour extends AbstractRoutingBehaviour {

    /**
     * Instantiates a new take all behaviour.
     */
    public TakeAllBehaviour() {

        this.joinBehaviour = new SimpleJoinBehaviour();
        this.splitBehaviour = new TakeAllSplitBehaviour();
    }
}

