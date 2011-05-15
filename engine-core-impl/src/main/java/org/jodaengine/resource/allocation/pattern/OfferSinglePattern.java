package org.jodaengine.resource.allocation.pattern;

import org.jodaengine.allocation.PushPattern;
import org.jodaengine.allocation.TaskAllocation;
import org.jodaengine.resource.worklist.AbstractWorklistItem;

import java.util.List;


/**
 * The Class OfferSinglePattern. See workflowpatterns.com.
 */
public class OfferSinglePattern implements PushPattern {

    @Override
    public void distributeWorkitems(TaskAllocation worklistService, List<AbstractWorklistItem> itemsToDistribute) {

        // TODO either implement this or remove it, as it may be the same as OfferMultiplePattern

    }

}
