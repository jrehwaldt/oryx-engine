package de.hpi.oryxengine.worklist;

import java.util.List;

import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.resource.Resource;

public interface Worklist {

    List<WorklistItem> getWorklistItems();
    
    void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource) throws OryxEngineException;

    void itemIsCompleted(WorklistItem worklistItem) throws OryxEngineException;
    
    void itemIsStarted(WorklistItem worklistItem) throws OryxEngineException;

    void addWorklistItem(WorklistItem worklistItem) throws OryxEngineException;
}
