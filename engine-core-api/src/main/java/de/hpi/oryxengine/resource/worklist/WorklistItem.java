/*
 * 
 */
package de.hpi.oryxengine.resource.worklist;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface WorklistItem.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "classifier")
public interface WorklistItem extends Task, Identifiable {
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    @JsonProperty
    @Nonnull WorklistItemState getStatus();
    
    /**
     * Gets the corresponding {@link Token} that created this {@link WorklistItem}.
     *
     * @return the corresponding token
     */
    @JsonIgnore
    @Nonnull Token getCorrespondingToken();
    
}
