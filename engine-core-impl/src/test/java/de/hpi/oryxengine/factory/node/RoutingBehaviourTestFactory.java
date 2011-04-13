package de.hpi.oryxengine.factory.node;

import static org.mockito.Mockito.mock;

import de.hpi.oryxengine.activity.AbstractActivity;

import de.hpi.oryxengine.plugin.activity.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleLogger;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.AndJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.XORSplitBehaviour;

/**
 * A factory for creating RoutingBeavhiourTest objects.
 */
public class RoutingBehaviourTestFactory extends AbstractNodeFactory {

    /**
     * Sets the activity.
     *
     * {@inheritDoc}
     */
    @Override
    public void setActivity() {
        
//        this.activity = mock(AbstractActivity.class);
        // TODO what to do here?
        
    }

    /**
     * Creates a new RoutingBehaviourTest object.
     *
     * @return the node
     */
    public Node createWithAndSplit() {
        setActivity();
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        return new NodeImpl(activityClazz, incomingBehaviour, outgoingBehaviour);
    }
    
    /**
     * Creates a new RoutingBehaviourTest object with a logger.
     *
     * @return the node
     */
    public Node createWithAndSplitAndLogger() {
        AbstractActivityLifecyclePlugin lifecycleLogger = ActivityLifecycleLogger.getInstance();
        setActivity();
//        activity.registerPlugin(lifecycleLogger);
        // TODO register plugin
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        return new NodeImpl(activityClazz, incomingBehaviour, outgoingBehaviour);
    }
    
    /**
     * Creates a new RoutingBehaviourTest object.
     *
     * @return the node
     */
    public Node createWithXORSplit() {
        setActivity();
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new XORSplitBehaviour();
        return new NodeImpl(activityClazz, incomingBehaviour, outgoingBehaviour);
    }
    
    /**
     * Creates a new RoutingBehaviourTest object.
     *
     * @return the node
     */
    public Node createWithAndJoin() {
        setActivity();
        incomingBehaviour = new AndJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        return new NodeImpl(activityClazz, incomingBehaviour, outgoingBehaviour);
    }
}
