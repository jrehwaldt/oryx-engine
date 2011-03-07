package de.hpi.oryxengine.factory;

import static org.mockito.Mockito.mock;
import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.join.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.impl.TakeAllSplitBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.impl.XORSplitBehaviour;

/**
 * A factory for creating RoutingBeavhiourTest objects.
 */
public class RoutingBehaviourTestFactory extends AbstractNodeFactory {

    public void setActivity() {

        this.activity = mock(Activity.class);
    }

    public Node createWithAndSplit() {

        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        return new NodeImpl(activity, incomingBehaviour, outgoingBehaviour);
    }
    
    public Node createWithXORSplit() {
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new XORSplitBehaviour();
        return new NodeImpl(activity, incomingBehaviour, outgoingBehaviour);
    }
}
