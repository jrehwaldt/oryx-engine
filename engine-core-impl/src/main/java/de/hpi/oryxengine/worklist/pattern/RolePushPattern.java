package de.hpi.oryxengine.worklist.pattern;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.WorklistItemImpl;
import de.hpi.oryxengine.worklist.WorklistItemState;
import de.hpi.oryxengine.worklist.WorklistQueue;

/**
 * 
 */
public class RolePushPattern implements Pattern {

    @Override
    public void execute(Task task, Token token, WorklistQueue worklistService) {

        WorklistItemImpl worklistItem = new WorklistItemImpl(task, token);
        worklistItem.setStatus(WorklistItemState.OFFERED);
        worklistService.addWorklistItem(worklistItem, task.getAssignedResources());

    }

}
