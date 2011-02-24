package de.hpi.oryxengine.processstructure;

import de.hpi.oryxengine.processstructure.Condition;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.Transition;

public class TransitionImpl implements Transition {

    Node destination;
    Node start;
    Condition condition;

    public TransitionImpl(Node start, Node destination, Condition c) {

        this.start = start;
        this.destination = destination;
        this.condition = c;

    }

    public Condition getCondition() {

        // TODO Auto-generated method stub
        return this.condition;
    }

    public Node getDestination() {

        return this.destination;
    }

    public Node getSource() {

        // TODO Auto-generated method stub
        return null;
    }

}
