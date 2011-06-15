package org.jodaengine.resource.allocation.pattern.detour;

import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.DetourPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.resource.worklist.WorklistServiceIntern;

/**
 * A reallocation pattern that allocates an executed item back to the participant that had the item before.
 * Its state changes from {@link WorklistItemState} EXECUTING to ALLOCATED.
 */
public class StatelessSelfReallocationPattern implements DetourPattern {

    @Override
    public void distributeItem(WorklistServiceIntern worklistService,
                               AbstractWorklistItem worklistItem,
                               AbstractResource<?> oldResource,
                               AbstractResource<?> newResource) {

        WorklistItemImpl item = WorklistItemImpl.asWorklistItemImpl(worklistItem);
        item.setStatus(WorklistItemState.ALLOCATED);
        // TODO should also clear variables that were set by filling out the form according to the item
    }

}
