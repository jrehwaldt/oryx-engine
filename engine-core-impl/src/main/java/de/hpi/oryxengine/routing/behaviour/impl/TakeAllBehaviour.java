package de.hpi.oryxengine.routing.behaviour.impl;

import de.hpi.oryxengine.routing.behaviour.AbstractRoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.join.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.impl.TakeAllSplitBehaviour;

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

