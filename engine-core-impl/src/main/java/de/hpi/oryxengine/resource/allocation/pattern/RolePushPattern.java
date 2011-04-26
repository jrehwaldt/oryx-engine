package de.hpi.oryxengine.resource.allocation.pattern;

import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * 
 */
public class RolePushPattern implements Pattern {

    @Override
    public void execute(Task task, Token token, TaskAllocation worklistService) {

        WorklistItemImpl worklistItem = new WorklistItemImpl(task, token);
        System.out.println("Created worklist item");
        worklistItem.setStatus(WorklistItemState.OFFERED);
        worklistService.addWorklistItem(worklistItem, task.getAssignedResources());
        System.out.println(worklistService.getWorklistItems(task.getAssignedResources().iterator().next()).size());
    }

}
