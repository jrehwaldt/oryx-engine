package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

/**
 * A factory for creating AbstractNode objects.
 */
public class AbstractNodeFactory {
    /** The behavior. */
    protected static RoutingBehaviour behaviour;
    /** The activity. */
    protected static Activity activity;
    /**
     * Creates the.
     *
     * @return the node
     */
    public static Node create() {
        setActivity();
        setBehaviour();
        return new NodeImpl(activity, behaviour);
    }
    /**
     * Sets the activity.
     */
    public static void setActivity() {
        activity = PrintingVariableActivityFactory.create();
    }
    /**
     * Sets the behavior.
     */
    public static void setBehaviour() {
        behaviour = TakeAllBehaviourFactory.create();
    }

}
