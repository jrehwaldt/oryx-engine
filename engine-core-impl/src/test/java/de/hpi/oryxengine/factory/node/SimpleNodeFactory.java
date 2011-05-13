package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.node.activity.Activity;
import de.hpi.oryxengine.node.incomingbehaviour.IncomingBehaviour;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.OutgoingBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;

/**
 * A simple Node which has the given activity blueprint.
 */
public final class SimpleNodeFactory {
    /**
     * Hidden constructor.
     */
    private SimpleNodeFactory() {

    }

    /**
     * Creates a new node with the given {@link ActivityBlueprint Activity Blueprint} and adds default.
     * 
     * @param activityBehavior
     *            -the {@link Activity activityBehavior}
     * @return the node with {@link SimpleJoinBehaviour} and {@link TakeAllSplitBehaviour}.
     */
    public static Node createSimpleNodeWith(Activity activityBehavior) {

        IncomingBehaviour incomingBehaviour = new SimpleJoinBehaviour();
        OutgoingBehaviour outgoingBehaviour = new TakeAllSplitBehaviour();

        return new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
    }
}
