package org.jodaengine.process.structure;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * The implementation of a {@link ControlFlow} that is.
 * A control flow is an edge between two nodes.
 */
public class ControlFlowImpl implements ControlFlow {

    /** The destination. E.g. where does the arrow point to. */
    private final Node destination;

    /** The start. E.g. Where does the arrow/edge originate. */
    private final Node source;

    /** The condition. The {@link ControlFlow} can only be executed with a true condition. */
    private final Condition condition;

    /**
     * Instantiates a new {@link ControlFlow} impl.
     * 
     * @param source
     *            the source node
     * @param destination
     *            the destination node
     * @param condition
     *            the condition
     */
    @JsonCreator
    public ControlFlowImpl(@JsonProperty("source") Node source,
                           @JsonProperty("destination") Node destination,
                           @JsonProperty("condition") Condition condition) {
        
        this.source = source;
        this.destination = destination;
        this.condition = condition;
    }

    /**
     * Gets the condition.
     *
     * @return the condition
     * @see org.jodaengine.process.structure.ControlFlow#getCondition()
     */
    @Override
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * Gets the destination.
     *
     * @return the destination
     * @see org.jodaengine.process.structure.ControlFlow#getDestination()
     */
    @Override
    public Node getDestination() {

        return this.destination;
    }

    /**
     * Gets the start.
     * 
     * @return the start
     */
    @Override
    public Node getSource() {

        return source;
    }

}
