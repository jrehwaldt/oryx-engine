package de.hpi.oryxengine.activity.impl;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.WorklistManager;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.TaskDistribution;

/**
 * The Implementation of a human task.
 * 
 * A user task is a typical “workflow” Task where a human performer performs the Task with the assistance of a software
 * application.
 */
public class HumanTaskActivity extends AbstractActivity {

    private Task task;

    /**
     * Default Constructor.
     * 
     * @param task - the task to distribute
     */
    public HumanTaskActivity(Task task) {

        this.task = task;
    }

    @Override
    protected void executeIntern(@Nonnull Token instance) {

        TaskDistribution taskDistribution = WorklistManager.getTaskDistribution();
        taskDistribution.distribute(task, instance);
    }

}
