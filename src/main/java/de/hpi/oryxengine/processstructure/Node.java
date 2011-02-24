package de.hpi.oryxengine.processstructure;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

/**
 * The Interface NodeInterface.
 */
public interface Node {

    // TODO: The node needs a join- and a split-behaviour, as you can imagine
    // nodes that have 3 incoming transitions and
    // 5 outgoing transitions in some crazy modelling language

    Activity getActivity();

    void setActivity(Activity activity);

    RoutingBehaviour getRoutingBehaviour();

    void setRoutingBehaviour(RoutingBehaviour behaviour);

    /**
     * Next.
     * 
     * @return the next Node(s) depending on the node (normal nodes vs. Splits which have multiple next nodes).
     */
    ArrayList<Transition> getTransitions();

    void transitionTo(Node node);

    String getId();

    void setId(String id);

}
