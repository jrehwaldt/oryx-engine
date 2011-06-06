package org.jodaengine.ext.debugging.api;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.Identifiable;

/**
 * This represents a container interface for a breakpoint.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface Breakpoint extends Switchable, Identifiable<UUID>, Serializable {
    
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
    @JsonProperty
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
