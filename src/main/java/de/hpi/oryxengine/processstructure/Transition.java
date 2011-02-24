package de.hpi.oryxengine.processstructure;

public interface Transition {

    Condition getCondition();

    Node getDestination();

    Node getSource();
}
