package de.hpi.oryxengine.node.activity.bpmn;

import java.util.List;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.allocation.PushPattern;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.node.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.allocation.pattern.AllocateSinglePattern;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

/**
 * The Implementation of a human task.
 * 
 * A user task is a typical workflow Task where a human performer performs a task with the assistance of a software
 * application.
 */
public class BpmnHumanTaskActivity extends AbstractActivity {

    @JsonProperty
    private CreationPattern creationPattern;
    
    @JsonProperty
    private PushPattern pushPattern;

    /**
     * Default Constructor.
     * 
     * @param task
     *            - the task to distribute
     */
    // TODO: CreationPattern einfügen
    public BpmnHumanTaskActivity(CreationPattern pattern, PushPattern pushPattern) {

        this.creationPattern = pattern;
        this.pushPattern = pushPattern;
    }

    @Override
    protected void executeIntern(@Nonnull Token token) {

        // creationPattern.createTask()

        // TODO @Thorben-Refactoring think about TaskDistribution and its necessity
//        TaskDistribution taskDistribution = ServiceFactory.getTaskDistribution();
//        taskDistribution.distribute(task, token);
        // TODO @Thorben-Refactoring should we use the TaskAllocation here?
        TaskAllocation service = ServiceFactory.getWorklistQueue();
        List<AbstractWorklistItem> items = creationPattern.createWorklistItems(token);
        pushPattern.distributeWorkitems(service, items);

        token.suspend();
    }

    @Override
    public void cancel() {

        // TODO change this as soon as we do not have the separation of task/worklistitem anymore (use methods from TaskAllocation)
        // TODO add this again, but maybe extend the creationPattern, to be able to remove the worklist items as well
//        for (AbstractResource<?> resource : task.getAssignedResources()) {
//            // remove all offered items
//            Iterator<AbstractWorklistItem> it = ((AbstractDefaultWorklist) resource.getWorklist())
//            .getLazyWorklistItems().iterator();
//
//            while (it.hasNext()) {
//                WorklistItemImpl item = (WorklistItemImpl) it.next();
//                if (item == task) {
//                    it.remove();
//                }
//            }
//        }
        
//        ServiceFactory.getWorklistQueue().removeWorklistItem(task, task.getAssignedResources());

    }
}
