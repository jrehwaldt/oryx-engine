package org.jodaengine.resource.allocation.pattern.push;

import org.jodaengine.resource.allocation.PushPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.resource.worklist.WorklistServiceIntern;


/**
 * The Class AllocateSinglePattern.
 */
public class AllocateSinglePattern implements PushPattern {

    @Override
    public void distributeItem(WorklistServiceIntern worklistService, AbstractWorklistItem itemToDistribute) {

        WorklistItemImpl item = WorklistItemImpl.asWorklistItemImpl(itemToDistribute);
        item.setStatus(WorklistItemState.ALLOCATED);
        worklistService.addWorklistItem(item, item.getAssignedResources());
    }
}
