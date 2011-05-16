package org.jodaengine.node.activity.bpmn;

import java.util.List;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.ServiceFactory;
import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.allocation.PushPattern;
import org.jodaengine.allocation.TaskAllocation;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.worklist.AbstractWorklistItem;


/**
 * The Implementation of a human task.
 * 
 * A user task is a typical workflow Task where a human performer performs a task with the assistance of a software
 * application. Upon its execution, worklist items are created with a {@link CreationPattern} and the distributed with a
 * {@link PushPattern}.
 */
public class BpmnHumanTaskActivity extends AbstractActivity {

    @JsonIgnore
    private CreationPattern creationPattern;

    @JsonIgnore
    private PushPattern pushPattern;

    /**
     * Default Constructor.
     * 
     * @param creationPattern
     *            the creation pattern to use
     * @param pushPattern
     *            the push pattern to use
     */
    public BpmnHumanTaskActivity(CreationPattern creationPattern, PushPattern pushPattern) {

        this.creationPattern = creationPattern;
        this.pushPattern = pushPattern;
    }

    @Override
    protected void executeIntern(@Nonnull Token token) {

        TaskAllocation service = ServiceFactory.getWorklistQueue();
        List<AbstractWorklistItem> items = creationPattern.createWorklistItems(token);
        pushPattern.distributeWorkitems(service, items);

        token.suspend();
    }

    @Override
    public void cancel() {

        // TODO change this as soon as we do not have the separation of task/worklistitem anymore (use methods from
        // TaskAllocation)
        // TODO add this again, but maybe extend the creationPattern, to be able to remove the worklist items as well
        // for (AbstractResource<?> resource : creationPattern.getAssignedResources()) {
        // // remove all offered items
        // Iterator<AbstractWorklistItem> it = ((AbstractDefaultWorklist) resource.getWorklist())
        // .getLazyWorklistItems().iterator();
        //
        // while (it.hasNext()) {
        // WorklistItemImpl item = (WorklistItemImpl) it.next();
        // if (item == task) {
        // it.remove();
        // }
        // }
        // }

        // ServiceFactory.getWorklistQueue().removeWorklistItem(task, task.getAssignedResources());

    }

    /**
     * This method is only designed for testing and should not be considered in the implementation of this
     * {@link BpmnHumanTaskActivity}.
     * 
     * @return the {@link CreationPattern}
     */
    public CreationPattern getCreationPattern() {

        return this.creationPattern;
    }
}
