package de.hpi.oryxengine.worklist;


/**
 * 
 */
public interface WorklistItem extends Task {
    
    WorklistItemState getStatus();
    
}
