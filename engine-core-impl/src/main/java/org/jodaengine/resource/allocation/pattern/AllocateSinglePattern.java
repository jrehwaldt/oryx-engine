package org.jodaengine.resource.allocation.pattern;

import org.jodaengine.allocation.PushPattern;
import org.jodaengine.allocation.TaskAllocation;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.jodaengine.resource.worklist.WorklistItemState;


/**
 * The Class AllocateSinglePattern.
 */
public class AllocateSinglePattern implements PushPattern {

    @Override
    public void distributeWorkitem(TaskAllocation worklistService, AbstractWorklistItem itemToDistribute) {

        WorklistItemImpl item = WorklistItemImpl.asWorklistItemImpl(itemToDistribute);
        item.setStatus(WorklistItemState.ALLOCATED);
        worklistService.addWorklistItem(item, item.getAssignedResources());
    }
}
