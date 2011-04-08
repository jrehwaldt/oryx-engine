package de.hpi.oryxengine.allocation.pattern;

import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * Simple Push Pattern - Only for testing.
 * 
 * TODO ... ab in den Testordner?!
 */
public class SimplePushPattern implements Pattern {

    @Override
    public void execute(Task task, Token token, TaskAllocation worklistService) {

        WorklistItemImpl worklistItem = new WorklistItemImpl(task, token);
        worklistItem.setStatus(WorklistItemState.ALLOCATED);
        worklistService.addWorklistItem(worklistItem, task.getAssignedResources());
    }
}
