package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.node.incomingbehaviour.IncomingBehaviour;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.OutgoingBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
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
     * Creates a new node with the given {@link ActivityBlueprint Activity Blueprint} and adds default
     * {@link IncomingBehaviour incoming} and {@link OutgoingBehaviour outgoing behavior}.
     * 
     * @param blueprint
     *            the blueprint
     * @return the node
     */
    public static Node createSimpleNodeWith(ActivityBlueprint blueprint) {

        IncomingBehaviour incomingBehaviour = new SimpleJoinBehaviour();
        OutgoingBehaviour outgoingBehaviour = new TakeAllSplitBehaviour();

        return new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
    }
}
