package de.hpi.oryxengine.node.activity.bpmn;

import java.util.Iterator;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.node.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.worklist.AbstractDefaultWorklist;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;

/**
 * The Implementation of a human task.
 * 
 * A user task is a typical workflow Task where a human performer performs a task with the assistance of a software
 * application.
 */
public class BpmnHumanTaskActivity extends AbstractActivity {

    @JsonProperty
    private Task task;

    /**
     * Default Constructor.
     * 
     * @param task
     *            - the task to distribute
     */
    // TODO: CreationPattern einfügen
    public BpmnHumanTaskActivity(Task task) {

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

        // TODO change this as soon as we do not have the separation of task/worklistitem anymore (use methods from TaskAllocation)
        for (AbstractResource<?> resource : task.getAssignedResources()) {
            // remove all offered items
            Iterator<AbstractWorklistItem> it = ((AbstractDefaultWorklist) resource.getWorklist())
            .getLazyOfferedWorklistItems().iterator();

            while (it.hasNext()) {
                WorklistItemImpl item = (WorklistItemImpl) it.next();
                if (item.getTask() == task) {
                    it.remove();
                }
            }
            
            // remove all allocated items
            it = ((AbstractDefaultWorklist) resource.getWorklist())
            .getLazyAllocatedWorklistItems().iterator();

            while (it.hasNext()) {
                WorklistItemImpl item = (WorklistItemImpl) it.next();
                if (item.getTask() == task) {
                    it.remove();
                }
            }
            
            // remove all executing items
            it = ((AbstractDefaultWorklist) resource.getWorklist())
            .getLazyExecutingWorklistItems().iterator();

            while (it.hasNext()) {
                WorklistItemImpl item = (WorklistItemImpl) it.next();
                if (item.getTask() == task) {
                    it.remove();
                }
            }
        }
        
//        ServiceFactory.getWorklistQueue().removeWorklistItem(task, task.getAssignedResources());

    }
}
