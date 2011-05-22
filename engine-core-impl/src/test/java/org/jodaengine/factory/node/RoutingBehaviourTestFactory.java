package org.jodaengine.factory.node;

import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.logger.TokenListenerLogger;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.AndJoinBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.XORSplitBehaviour;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;


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
    public void setActivityBlueprint() {
        
        activityBehavior = new NullActivity();
        
    }

    /**
     * Creates a new RoutingBehaviourTest object.
     *
     * @return the node
     */
    public Node createWithAndSplit() {
        setActivityBlueprint();
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        return new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
    }
    
    /**
     * Creates a new RoutingBehaviourTest object with a logger.
     *
     * @return the node
     */
    public Node createWithAndSplitAndLogger() {
        AbstractTokenListener lifecycleLogger = TokenListenerLogger.getInstance();
        setActivityBlueprint();
//        activity.registerPlugin(lifecycleLogger);
        // TODO register plugin
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        return new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
    }
    
    /**
     * Creates a new RoutingBehaviourTest object.
     *
     * @return the node
     */
    public Node createWithXORSplit() {
        setActivityBlueprint();
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new XORSplitBehaviour();
        return new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
    }
    
    /**
     * Creates a new RoutingBehaviourTest object.
     *
     * @return the node
     */
    public Node createWithAndJoin() {
        setActivityBlueprint();
        incomingBehaviour = new AndJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        return new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
    }
}
