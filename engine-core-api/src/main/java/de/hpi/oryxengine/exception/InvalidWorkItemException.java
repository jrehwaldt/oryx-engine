package de.hpi.oryxengine.exception;

import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * This is an exception stating that a work item does not exist.
 * 
 * @author Jan Rehwaldt
 */
public class InvalidWorkItemException extends JodaEngineException {
    private static final long serialVersionUID = -2174770498861525988L;

    private static final String MESSAGE = "The requested work item is not available or was removed.";

    private final UUID workItemID;

    /**
     * Instantiates a new invalid item exception.
     * 
     * @param workItemID the id of the resource that is not available
     */
    public InvalidWorkItemException(@Nonnull UUID workItemID) {
        super(MESSAGE);
        
        this.workItemID = workItemID;
    }
    
    /**
     * Gets the work item id.
     *
     * @return the work item id
     */
    public UUID getWorkItemID() {
        return workItemID;
    }
    
    @Override
    @Nonnull
    public String toString() {
        return String.format("WorkItem[id: %s]", getWorkItemID());
    }
}
