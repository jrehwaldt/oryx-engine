package de.hpi.oryxengine.activity.impl;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Implementation of a human task.
 * 
 * A user task is a typical workflow Task where a human performer performs the Task with the assistance of a software
 * application.
 */
public class HumanTaskActivity extends AbstractActivity {

    /** The task. */
    private Task task;

    /**
     * Default Constructor.
     * 
     * @param task - the task to distribute
     */
    // TODO: CreationPattern einfügen
    public HumanTaskActivity(Task task) {

        this.task = task;
    }

    @Override
    protected void executeIntern(@Nonnull Token token) {

//        creationPattern.createTask()
        
        TaskDistribution taskDistribution = ServiceFactory.getTaskDistribution();
        taskDistribution.distribute(task, token);
        
        token.suspend();
    }
    
    @Override
    public void signal(Token token) {

        // BlaBla
    }
}
