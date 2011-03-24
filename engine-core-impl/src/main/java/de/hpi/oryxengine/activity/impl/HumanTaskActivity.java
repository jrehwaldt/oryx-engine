package de.hpi.oryxengine.activity.impl;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.TaskDistribution;

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(@Nonnull Token instance) {

//        creationPattern.createTask()
        
        TaskDistribution taskDistribution = ServiceFactory.getTaskDistribution();
        taskDistribution.distribute(task, instance);
        
        instance.suspend();
    }
    
    @Override
    public void signal(Token token) {

        // BlaBla
    }
}
