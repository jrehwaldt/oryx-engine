package org.jodaengine.resource.allocation.pattern.push;

import org.jodaengine.resource.allocation.PushPattern;
import org.jodaengine.resource.allocation.TaskAllocation;
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
