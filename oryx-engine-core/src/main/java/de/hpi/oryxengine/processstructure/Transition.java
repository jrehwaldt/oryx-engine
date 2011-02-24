package de.hpi.oryxengine.processstructure;

import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public interface Transition {

    Condition getCondition();

    Node getDestination();

    Node getSource();
}
