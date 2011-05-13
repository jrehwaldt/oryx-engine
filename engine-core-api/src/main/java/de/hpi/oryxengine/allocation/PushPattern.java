package de.hpi.oryxengine.allocation;

import java.util.List;

import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

/**
 * The Interface PushPattern according to www.workflowpatterns.com
 */
public interface PushPattern {
    
    void distributeWorkitems(TaskAllocation worklistService, List<AbstractWorklistItem> itemsToDistribute);

}
