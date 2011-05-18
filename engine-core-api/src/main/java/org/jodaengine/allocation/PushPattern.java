package org.jodaengine.allocation;

import org.jodaengine.resource.worklist.AbstractWorklistItem;

/**
 * The Interface PushPattern according to www.workflowpatterns.com.
 * Push patterns describe the way tasks are offered or allocated to resources.
 * 
 * web reference:
 * http://www.workflowpatterns.com/patterns/resource/#Push
 */
public interface PushPattern {

    /**
     * Distributes worklist items using the supplied worklistService by a strategy that is re-implemented in every
     * subclass.
     *
     * @param worklistService the worklist service
     * @param itemToDistribute the item to distribute
     */
    void distributeWorkitem(TaskAllocation worklistService, AbstractWorklistItem itemToDistribute);

}
