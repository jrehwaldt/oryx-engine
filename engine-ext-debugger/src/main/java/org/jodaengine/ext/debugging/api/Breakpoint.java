package org.jodaengine.ext.debugging.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.Identifiable;

/**
 * This represents a container class for a breakpoint, which will be available in the
 * {@link Node} attribute set. Static getter methods are provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public interface Breakpoint extends Switchable, Identifiable<UUID> {
    
    /**
     * Returns the {@link Node} this breakpoint is bound to.
     * 
     * @return a node
     */
    @Nonnull Node getNode();
    
    /**
     * Returns the {@link ActivityState} this breakpoint is bound to.
     * 
     * @return a state
     */
    @Nonnull ActivityState getState();
    
    /**
     * Sets a {@link BreakpointCondition}, which is considered when evaluating whether
     * this breakpoint matches to a {@link Token} state.
     * 
     * @param condition a condition
     */
    void setCondition(@Nonnull BreakpointCondition condition);
    
    /**
     * Returns a {@link BreakpointCondition}, which is considered when evaluating whether
     * this breakpoint matches to a {@link Token} state.
     * 
     * @return a condition, max be null
     */
    @Nullable BreakpointCondition getCondition();
    
    /**
     * Checks whether the breakpoint will match the current token state.
     * 
     * Properly considers any eventually defined condition.
     * 
     * @param token the token
     * @return a boolean, whether this token is matched by this breakpoint
     */
    boolean matches(@Nonnull Token token);
}
