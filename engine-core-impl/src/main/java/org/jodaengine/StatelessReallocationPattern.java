package org.jodaengine;

import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.DetourPattern;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.jodaengine.resource.worklist.WorklistItemState;

/**
 * The stateless reallocation pattern that allocates an item to a specific participant.
 */
public class StatelessReallocationPattern implements DetourPattern {

    @Override
    public void distribute(AbstractWorklistItem worklistItem, AbstractResource<?> resource) {

        WorklistItemImpl item = WorklistItemImpl.asWorklistItemImpl(worklistItem);
        item.setStatus(WorklistItemState.ALLOCATED);
    }

}
