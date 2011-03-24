package de.hpi.oryxengine.resource.worklist;

import java.util.Collections;
import java.util.List;

import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.WorklistItem;

/**
 * 
 */
public class EmptyWorklist extends AbstractWorklist {
    
    static final String EXCEPTION_MESSAGE = "The resource object has no special type."; 
    
    @Override
    public List<WorklistItem> getWorklistItems() {

        List<WorklistItem> emptyWorklistItems = Collections.emptyList();
        return Collections.unmodifiableList(emptyWorklistItems);
    }

    @Override
    public void itemIsCompleted(WorklistItem worklistItem) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);        
    }

    @Override
    public void itemIsStarted(WorklistItem worklistItem) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);        
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);        
    }

    @Override
    public void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource) {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }
}
