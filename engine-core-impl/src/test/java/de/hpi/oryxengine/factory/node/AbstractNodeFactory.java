package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.node.activity.fun.PrintingVariableActivity;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.plugin.activity.AbstractTokenPlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleLogger;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * A factory for creating AbstractNode objects.
 */
abstract class AbstractNodeFactory {
    /** The behavior. */
    protected IncomingBehaviour incomingBehaviour;
    /** The behavior. */
    protected OutgoingBehaviour outgoingBehaviour;
    /** The activity. */
    protected ActivityBlueprint blueprint;

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
        Node node = new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
        return node;
    }

    /**
     * Sets the activity. It sets a default activity which is the printvariable activity.
     */
    public void setActivityBlueprint() {

        Class<?>[] constructorSig = {String.class};
        Object[] params = {"result"};
        blueprint = new ActivityBlueprintImpl(PrintingVariableActivity.class, constructorSig, params);
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
        return new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
    }

}
