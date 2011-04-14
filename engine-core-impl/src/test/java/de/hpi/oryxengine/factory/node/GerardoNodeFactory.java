package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.plugin.activity.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleLogger;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * A simple dumb Gerardo Node Factory which is sort of lame.
 */
public final class GerardoNodeFactory {
    /**
     * Hidden constructor.
     */
    private GerardoNodeFactory() {

    }

    public static Node createSimpleNodeWith(ActivityBlueprint blueprint) {

        IncomingBehaviour incomingBehaviour = new SimpleJoinBehaviour();
        OutgoingBehaviour outgoingBehaviour = new TakeAllSplitBehaviour();

        return new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
    }

    public static Node attachLoggerPluginTo(Node node) {

        AbstractActivityLifecyclePlugin lifecycleLogger = ActivityLifecycleLogger.getInstance();

        try {

            // AbstractActivity abstractActivity = ((AbstractActivity) node.getActivity());
            // abstractActivity.registerPlugin(lifecycleLogger);
            // TODO add plugins
        } catch (ClassCastException classCastException) {
            String exceptionMessage = "The activity class " + node.getActivityBlueprint().getActivityClass().getName()
                + " of the node " + node.getID() + " is not an AbstractActivity.";
            throw new ClassCastException(exceptionMessage);
        }

        return node;
    }
}
