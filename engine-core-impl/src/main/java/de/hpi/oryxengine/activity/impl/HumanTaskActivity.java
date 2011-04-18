package de.hpi.oryxengine.activity.impl;

import java.util.Iterator;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.AbstractDeferredActivity;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.allocation.TaskImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.AbstractDefaultWorklist;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;

/**
 * The Implementation of a human task.
 * 
 * A user task is a typical workflow Task where a human performer performs the Task with the assistance of a software
 * application.
 */
public class HumanTaskActivity extends AbstractDeferredActivity {

    private Task task;

    /**
     * Default Constructor.
     * 
     * @param task
     *            - the task to distribute
     */
    // TODO: CreationPattern einfügen
    public HumanTaskActivity(Task task) {

        this.task = new TaskImpl(task);
    }

    @Override
    protected void executeIntern(@Nonnull Token token) {

        // creationPattern.createTask()

        TaskDistribution taskDistribution = ServiceFactory.getTaskDistribution();
        taskDistribution.distribute(task, token);

        token.suspend();
    }

    @Override
    public void cancel() {

        // TODO move this to worklist manager
        for (AbstractResource<?> resource : task.getAssignedResources()) {
            Iterator<AbstractWorklistItem> it = ((AbstractDefaultWorklist) resource.getWorklist()).getLazyWorklistItems()
            .iterator();

            while (it.hasNext()) {
                WorklistItemImpl item = (WorklistItemImpl) it.next();
                if (item.getTask() == task) {
                    it.remove();
                }
            }
            // for (WorklistItem item : resource.getWorklist().getLazyWorklistItems())
            // resource.getWorklist().getLazyWorklistItems().remove(task);
        }

    }
}
