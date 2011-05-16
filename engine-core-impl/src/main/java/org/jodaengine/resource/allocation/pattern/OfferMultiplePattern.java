package org.jodaengine.resource.allocation.pattern;

import java.util.List;

import org.jodaengine.allocation.PushPattern;
import org.jodaengine.allocation.TaskAllocation;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.jodaengine.resource.worklist.WorklistItemState;


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
