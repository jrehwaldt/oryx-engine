package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;

/**
 * A factory for creating AbstractNode objects.
 */
abstract class AbstractNodeFactory {
    /** The behavior. */
    protected RoutingBehaviour behaviour;
    /** The activity. */
    protected Activity<?> activity;

    /**
     * Creates the.
     * 
     * @return the node
     */
    public Node create() {

        this.setActivity();
        this.setBehaviour();
        return new NodeImpl(activity, behaviour);
    }

    /**
     * Sets the activity.
     * It sets a default activity which is the printvariable activity.
     */
    public void setActivity() {

        activity = new PrintingVariableActivity("result");
    }

    /**
     * Sets the behavior.
     */
    public void setBehaviour() {

        behaviour = new TakeAllBehaviour();
    }

}
