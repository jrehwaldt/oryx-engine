package de.hpi.oryxengine.resource.allocation.pattern;

import java.util.List;

import de.hpi.oryxengine.allocation.PushPattern;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

/**
 * The Class OfferSinglePattern. See workflowpatterns.com.
 */
public class OfferSinglePattern implements PushPattern {

    @Override
    public void distributeWorkitems(TaskAllocation worklistService, List<AbstractWorklistItem> itemsToDistribute) {

        // TODO either implement this or remove it, as it may be the same as OfferMultiplePattern

    }

}
