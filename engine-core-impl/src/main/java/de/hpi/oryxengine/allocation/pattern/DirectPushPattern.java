package de.hpi.oryxengine.allocation.pattern;

import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.worklist.WorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * DirectPushPattern receives a task and converts it to a {@link WorklistItem}. This {@link WorklistItem} is allocated
 * directly to a certain resource according to this pattern.
 * 
 * This implementation represents the pattern shown in here:
 * http://www.workflowpatterns.com/patterns/resource/push/wrp14.php
 * 
 */
public class DirectPushPattern implements Pattern {

    @Override
    public void execute(Task task, Token token, TaskAllocation worklistService) {

        WorklistItemImpl worklistItem = new WorklistItemImpl(task, token);
        worklistItem.setStatus(WorklistItemState.ALLOCATED);
        worklistService.addWorklistItem(worklistItem, task.getAssignedResources());
    }
}
