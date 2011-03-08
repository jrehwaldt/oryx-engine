package de.hpi.oryxengine.worklist;

/**
 * This Enum represents the different states of a {@link WorklistItem}.
 */
public enum WorklistItemState {

    /** Means that the item is offered to a (single or multi) resource and can be claimed. */
    OFFERED,

    /** Means that the item is claimed by or delegated to a resource. */
    ALLOCATED,
    
    /** Means that the allocated item was started and is executed by the resource. */
    EXECUTING,

    /** Means that the item has been executed and finished. */
    COMPLETED
}
