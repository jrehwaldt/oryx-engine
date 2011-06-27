package org.jodaengine.ext.debugging.shared;

import java.io.Serializable;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.token.Token;

/**
 * This represents a history entry, which maps a {@link ControlFlow} to a
 * {@link Token} and helps keeping track of the process execution path.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-14
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public final class PathHistoryEntry implements Serializable {
    private static final long serialVersionUID = -1922922944145687463L;
    
    private final ControlFlow path;
    private final Token token;
    
    /**
     * Default constructor.
     * 
     * @param path the chosen path
     * @param token the processing token
     */
    PathHistoryEntry(@Nonnull ControlFlow path,
                     @Nonnull Token token) {
        
        this.path = path;
        this.token = token;
    }
    
    /**
     * @return the path
     */
    public @Nonnull ControlFlow getPath() {
        return this.path;
    }
    
    /**
     * @return the token
     */
    public @Nonnull Token getToken() {
        return this.token;
    }
}
