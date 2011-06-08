package org.jodaengine.process.structure;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

/**
 * The Interface for control flows. Control flows are the edges between nodes.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface ControlFlow {

    /**
     * Gets the condition that is connected to the control flow.
     * 
     * @return the condition
     */
    @Nonnull
    @JsonProperty
    Condition getCondition();

    /**
     * Gets the destination of the control flow.
     * 
     * @return the destination node of the edge.
     */
    @Nonnull
    @JsonProperty
    Node getDestination();

    /**
     * Gets the source of the control flow.
     * 
     * @return the source node of the edge.
     */
    @Nonnull
    @JsonIgnore
    Node getSource();
}
