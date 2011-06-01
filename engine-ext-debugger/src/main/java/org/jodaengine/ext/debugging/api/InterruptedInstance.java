package org.jodaengine.ext.debugging.api;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.Identifiable;

/**
 * This describes a container for interrupted {@link Token}s,
 * which were matched by a {@link Breakpoint}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public interface InterruptedInstance extends Identifiable<UUID> {
    
    /**
     * @return the {@link Token}, which was interrupted
     */
    @JsonProperty
    @Nonnull
    Token getInterruptedToken();
    
    /**
     * @return the {@link Breakpoint}, which caused the interruption
     */
    @JsonProperty
    @Nonnull
    Breakpoint getCausingBreakpoint();
    
    /**
     * @return the {@link AbstractProcessInstance}, which was interrupted
     */
    @JsonIgnore
    @Nonnull
    AbstractProcessInstance getInterruptedInstance();
    
}
