package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.plugin.activity.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleLogger;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * 
 */
public class GerardoNodeFactory {

    
    public static Node createSimpleNodeWith(Activity activity) {

        IncomingBehaviour incomingBehaviour = new SimpleJoinBehaviour();
        OutgoingBehaviour outgoingBehaviour = new TakeAllSplitBehaviour();
        
        return new NodeImpl(activity, incomingBehaviour, outgoingBehaviour);
    }
    
    public static Node attachLoggerPluginTo(Node node) {
        
        AbstractActivityLifecyclePlugin lifecycleLogger = ActivityLifecycleLogger.getInstance();

        try {

            AbstractActivity abstractActivity = ((AbstractActivity) node.getActivity());
            abstractActivity.registerPlugin(lifecycleLogger);
        } catch (ClassCastException classCastException) {
            String exceptionMessage = "The activity class " + node.getActivity().getClass().getName() + " of the node "
                                        + node.getID() + " is not an AbstractActivity.";
            throw new ClassCastException(exceptionMessage);
        }
        
        return node;
    }
}
