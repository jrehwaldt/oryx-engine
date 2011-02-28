package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;

/**
 * A factory for creating TakeAllBehaviour objects.
 */
public class TakeAllBehaviourFactory extends AbstractRoutingBehaviourFactory {
    /**
     * Creates the Routing Behavior.
     *
     * @return the routing behavior
     */
    public static RoutingBehaviour create() {
        return new TakeAllBehaviour();
    }

}
