package org.jodaengine.ext.debugging.api;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.structure.Node;

/**
 * This represents a container interface for a {@link Breakpoint}, which is bound to a {@link Node}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-02
 */
public interface NodeBreakpoint extends Breakpoint {

    /**
     * Returns the {@link Node} this breakpoint is bound to.
     * 
     * @return a node
     */
    @JsonProperty
    @Nonnull Node getNode();

    /**
     * Returns the {@link ActivityState} this breakpoint is bound to.
     * 
     * @return a state
     */
    @JsonProperty
    @Nonnull ActivityState getState();
}
