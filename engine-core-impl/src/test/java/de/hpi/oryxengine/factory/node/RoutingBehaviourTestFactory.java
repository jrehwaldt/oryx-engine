package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.plugin.activity.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleLogger;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
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
        AbstractActivityLifecyclePlugin lifecycleLogger = ActivityLifecycleLogger.getInstance();
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
