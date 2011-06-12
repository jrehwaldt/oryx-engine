package org.jodaengine.ext.debugging.api;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
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
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface InterruptedInstance extends Identifiable<UUID>, Serializable {
    
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
    @Nullable
    Breakpoint getCausingBreakpoint();
    
    /**
     * @return the {@link AbstractProcessInstance}, which was interrupted
     */
    @JsonProperty
    @Nonnull
    AbstractProcessInstance getInterruptedInstance();
    
}
