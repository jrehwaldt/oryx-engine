package org.jodaengine.factory.node;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.custom.PrintingVariableActivity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.plugin.activity.AbstractTokenPlugin;
import org.jodaengine.plugin.activity.ActivityLifecycleLogger;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;


/**
 * A factory for creating AbstractNode objects.
 */
abstract class AbstractNodeFactory {
    /** The behavior. */
    protected IncomingBehaviour incomingBehaviour;
    /** The behavior. */
    protected OutgoingBehaviour outgoingBehaviour;
    /** The activity. */
    protected Activity activityBehavior;

    /**
     * Creates the activity for a given process instance and puts the constructor signature and the parameter values to
     * the given process instance.
     * 
     * @param instance
     *            the instance
     * @return the node
     */
    public Node create() {

        this.setActivityBlueprint();
        this.setBehaviour();
        Node node = new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
        return node;
    }

    /**
     * Sets the activity. It sets a default activity which is the printvariable activity.
     */
    public void setActivityBlueprint() {

        activityBehavior = new PrintingVariableActivity("result");
    }

    /**
     * Sets the behavior.
     */
    public void setBehaviour() {

        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
    }

    /**
     * Creates a new Node object with a logger.
     * 
     * @return the node
     */
    public Node createWithLogger() {

        AbstractTokenPlugin lifecycleLogger = ActivityLifecycleLogger.getInstance();
        this.setActivityBlueprint();
        // activity.registerPlugin(lifecycleLogger);
        // TODO what to do with plugins?
        this.setBehaviour();
        return new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
    }

}
