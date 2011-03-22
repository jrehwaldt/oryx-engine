package de.hpi.oryxengine.resource.worklist;

import java.util.Collections;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.WorklistItem;

/**
 * 
 */
public class EmptyWorklist extends AbstractWorklist {
    
    static final String EXCEPTION_MESSAGE = "The resource object has no special type."; 
    
    @Override
    public List<WorklistItem> getWorklistItems() {

        return Collections.emptyList();
    }

    @Override
    public void itemIsCompleted(WorklistItem worklistItem)
    throws OryxEngineException {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);        
    }

    @Override
    public void itemIsStarted(WorklistItem worklistItem)
    throws OryxEngineException {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);        
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem)
    throws OryxEngineException {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);        
    }

    @Override
    public void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource)
    throws OryxEngineException {

        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
        
    }

}
