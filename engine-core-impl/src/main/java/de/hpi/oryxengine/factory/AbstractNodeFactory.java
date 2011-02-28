package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

/**
 * A factory for creating AbstractNode objects.
 */
abstract class AbstractNodeFactory {
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

        activity = new PrintingVariableActivity("result");
    }

    /**
     * Sets the behavior.
     */
    public static void setBehaviour() {

        behaviour = new TakeAllBehaviour();
    }

}
