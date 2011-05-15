package org.jodaengine.factory.node;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;


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
