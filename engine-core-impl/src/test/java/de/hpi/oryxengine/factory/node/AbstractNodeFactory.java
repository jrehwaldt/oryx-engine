package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.plugin.activity.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleLogger;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * A factory for creating AbstractNode objects.
 */
abstract class AbstractNodeFactory {
    /** The behavior. */
    protected IncomingBehaviour incomingBehaviour;
    /** The behavior. */
    protected OutgoingBehaviour outgoingBehaviour;
    /** The activity. */
    protected Class<? extends Activity> activityClazz;

    /**
     * Creates the.
     * 
     * @return the node
     */
    public Node create() {

        this.setActivity();
        this.setBehaviour();
        return new NodeImpl(activityClazz, incomingBehaviour, outgoingBehaviour);
    }

    /**
     * Sets the activity. It sets a default activity which is the printvariable activity.
     */
    public void setActivity() {

//        activity = new PrintingVariableActivity("result");
        // TODO add parameter
        activityClazz = PrintingVariableActivity.class;
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
        AbstractActivityLifecyclePlugin lifecycleLogger = ActivityLifecycleLogger.getInstance();
        this.setActivity();
//        activity.registerPlugin(lifecycleLogger);
        // TODO what to do with plugins?
        this.setBehaviour();
        return new NodeImpl(activityClazz, incomingBehaviour, outgoingBehaviour);
    }

}
