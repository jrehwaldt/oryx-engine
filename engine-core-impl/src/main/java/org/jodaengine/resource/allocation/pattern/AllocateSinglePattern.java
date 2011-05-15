package org.jodaengine.resource.allocation.pattern;

import org.jodaengine.allocation.PushPattern;
import org.jodaengine.allocation.TaskAllocation;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.jodaengine.resource.worklist.WorklistItemState;

import java.util.List;


/**
 * The Class AllocateSinglePattern.
 */
public class AllocateSinglePattern implements PushPattern {

    @Override
    public void distributeWorkitems(TaskAllocation worklistService, List<AbstractWorklistItem> itemsToDistribute) {


        if (itemsToDistribute.size() > 1) {
            // TODO throw an error
        }
        for (AbstractWorklistItem item : itemsToDistribute) {
            ((WorklistItemImpl) item).setStatus(WorklistItemState.ALLOCATED);
            worklistService.addWorklistItem(item, item.getAssignedResources());
        }
        
        
    }

}
