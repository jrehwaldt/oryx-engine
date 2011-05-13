package de.hpi.oryxengine.resource.allocation.pattern;

import java.util.List;

import de.hpi.oryxengine.allocation.PushPattern;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

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
