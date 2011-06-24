package org.jodaengine.ext.debugging.shared;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
public final class PathHistoryEntry {
    
    private final ControlFlow path;
    private final UUID tokenID;
    private final UUID parentTokenID;
    
    /**
     * Default constructor.
     * 
     * @param path the chosen path
     * @param token the processing token
     */
    PathHistoryEntry(@Nonnull ControlFlow path,
                     @Nonnull Token token) {
        
        this.path = path;
        this.tokenID = token.getID();
        
        if (token.getParentToken() != null) {
            this.parentTokenID = token.getParentToken().getID();
        } else {
            this.parentTokenID = null;
        }
    }
    
    /**
     * @return the path
     */
    public @Nonnull ControlFlow getPath() {
        return path;
    }
    
    /**
     * @return the token's id
     */
    public @Nonnull UUID getTokenID() {
        return tokenID;
    }
    
    /**
     * @return the parental token's id
     */
    public @Nullable UUID getParentTokenID() {
        return parentTokenID;
    }
}
