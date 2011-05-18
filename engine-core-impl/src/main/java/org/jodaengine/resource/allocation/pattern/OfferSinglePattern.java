package org.jodaengine.resource.allocation.pattern;

import org.jodaengine.allocation.PushPattern;
import org.jodaengine.allocation.TaskAllocation;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.jodaengine.resource.worklist.WorklistItemState;

/**
 * The Class OfferSinglePattern. See workflowpatterns.com.
 */
public class OfferSinglePattern implements PushPattern {

    @Override
    public void distributeWorkitem(TaskAllocation worklistService, AbstractWorklistItem itemToDistribute) {

        WorklistItemImpl item = WorklistItemImpl.asWorklistItemImpl(itemToDistribute);
        item.setStatus(WorklistItemState.OFFERED);
        if (item.getAssignedResources().size() != 1) {
            throw new JodaEngineRuntimeException(
                "There should have been one resource for the item, but there have been "
                + item.getAssignedResources().size());
        }
        worklistService.addWorklistItem(item, item.getAssignedResources());
    }
}
