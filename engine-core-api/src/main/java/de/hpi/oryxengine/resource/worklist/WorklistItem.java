/*
 * 
 */
package de.hpi.oryxengine.resource.worklist;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Interface WorklistItem.
 */
public interface WorklistItem extends Task {
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    @Nonnull WorklistItemState getStatus();
    
    /**
     * Gets the corresponding {@link Token} that created this {@link WorklistItem}.
     *
     * @return the corresponding token
     */
    @Nonnull Token getCorrespondingToken();
    
}
