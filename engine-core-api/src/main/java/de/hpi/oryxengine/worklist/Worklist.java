package de.hpi.oryxengine.worklist;

import java.util.List;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.resource.Resource;

/**
 * The Interface for the work list.
 */
public interface Worklist {

    List<WorklistItem> getWorklistItems();
    
    void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource) throws DalmatinaException;

    void itemIsCompleted(WorklistItem worklistItem) throws DalmatinaException;
    
    void itemIsStarted(WorklistItem worklistItem) throws DalmatinaException;

    void addWorklistItem(WorklistItem worklistItem) throws DalmatinaException;
}
