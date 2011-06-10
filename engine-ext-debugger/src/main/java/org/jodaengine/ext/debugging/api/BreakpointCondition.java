package org.jodaengine.ext.debugging.api;

import java.io.Serializable;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.process.token.Token;

/**
 * This interface represents a condition, which may be attached to a {@link Breakpoint}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface BreakpointCondition extends Serializable {
    
    /**
     * When this method is called the condition needs to be evaluated.
     * If it evaluates to true this is returned, false respectively.
     * 
     * True is furthermore returned if the condition fails to be evaluated.
     * 
     * @param token the token
     * @return whether the condition is true
     */
    boolean evaluate(@Nonnull Token token);
}
