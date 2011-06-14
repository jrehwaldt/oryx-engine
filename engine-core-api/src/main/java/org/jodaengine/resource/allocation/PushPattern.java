package org.jodaengine.resource.allocation;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistServiceIntern;

/**
 * The Interface PushPattern according to www.workflowpatterns.com.
 * Push patterns describe the way tasks are offered or allocated to resources.
 * 
 * web reference:
 * http://www.workflowpatterns.com/patterns/resource/#Push
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface PushPattern {

    /**
     * Distributes worklist items using the supplied worklistService by a strategy that is re-implemented in every
     * subclass.
     *
     * @param worklistService the worklist service
     * @param itemToDistribute the item to distribute
     */
    void distributeWorkitem(WorklistServiceIntern worklistService, AbstractWorklistItem itemToDistribute);

}
