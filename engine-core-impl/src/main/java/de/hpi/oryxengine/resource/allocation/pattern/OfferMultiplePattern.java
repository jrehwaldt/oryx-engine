package de.hpi.oryxengine.resource.allocation.pattern;

import java.util.List;

import de.hpi.oryxengine.allocation.PushPattern;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * The Class OfferMultiplePattern. See workflowpatterns.com.
 */
public class OfferMultiplePattern implements PushPattern {

    @Override
    public void distributeWorkitems(TaskAllocation worklistService, List<AbstractWorklistItem> itemsToDistribute) {

        for (AbstractWorklistItem item : itemsToDistribute) {
            ((WorklistItemImpl) item).setStatus(WorklistItemState.OFFERED);
            worklistService.addWorklistItem(item, item.getAssignedResources());
        }
        
    }


}
