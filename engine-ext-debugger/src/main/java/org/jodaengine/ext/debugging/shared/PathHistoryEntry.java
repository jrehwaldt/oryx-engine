package org.jodaengine.ext.debugging.shared;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;
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
    private final UUID tokenID;
    private Set<UUID> parentalTokenIDs;
    
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
        this.parentalTokenIDs = new HashSet<UUID>();
        
        while (token.getParentToken() != null) {
            token = token.getParentToken();
            this.parentalTokenIDs.add(token.getID());
        }
    }
    
    /**
     * @return the path
     */
    @JsonProperty
    public @Nonnull ControlFlow getPath() {
        return this.path;
    }
    
    /**
     * @return the token
     */
    @JsonProperty
    public @Nonnull UUID getTokenID() {
        return this.tokenID;
    }
    
    /**
     * @return the parental token IDs
     */
    @JsonProperty
    public @Nonnull Set<UUID> getParentalTokenIDs() {
        return this.parentalTokenIDs;
    }
}
