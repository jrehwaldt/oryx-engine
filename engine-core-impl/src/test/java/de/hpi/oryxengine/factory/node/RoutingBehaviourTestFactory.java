package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.node.activity.NullActivity;
import de.hpi.oryxengine.node.incomingbehaviour.AndJoinBehaviour;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.XORSplitBehaviour;
import de.hpi.oryxengine.plugin.activity.AbstractTokenPlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleLogger;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;

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
        
        blueprint = new ActivityBlueprintImpl(NullActivity.class);
        
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
        return new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
    }
    
    /**
     * Creates a new RoutingBehaviourTest object with a logger.
     *
     * @return the node
     */
    public Node createWithAndSplitAndLogger() {
        AbstractTokenPlugin lifecycleLogger = ActivityLifecycleLogger.getInstance();
        setActivityBlueprint();
//        activity.registerPlugin(lifecycleLogger);
        // TODO register plugin
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        return new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
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
        return new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
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
        return new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
    }
}
