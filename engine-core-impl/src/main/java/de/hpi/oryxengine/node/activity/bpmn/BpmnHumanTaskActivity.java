package de.hpi.oryxengine.node.activity.bpmn;

import java.util.Iterator;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskAllocation;
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
    private CreationPattern pattern;
    
    private String subject;
    
    private String description;
    
    private Form form;
    
    private AbstractResource<?>[] resourcesToAssignTo;

    /**
     * Default Constructor.
     * 
     * @param task
     *            - the task to distribute
     */
    // TODO: CreationPattern einfügen
    public BpmnHumanTaskActivity(CreationPattern pattern, String subject, String description, Form form, AbstractResource<?>[] resourcesToAssignTo) {

        this.pattern = pattern;
        this.subject = subject;
        this.description = description;
        this.form = form;
        this.resourcesToAssignTo = resourcesToAssignTo;
    }

    @Override
    protected void executeIntern(@Nonnull Token token) {

        // creationPattern.createTask()

        // TODO @Thorben-Refactoring think about TaskDistribution and its necessity
//        TaskDistribution taskDistribution = ServiceFactory.getTaskDistribution();
//        taskDistribution.distribute(task, token);
        // TODO @Thorben-Refactoring should we use the TaskAllocation here?
        TaskAllocation service = ServiceFactory.getWorklistQueue();
        pattern.createWorklistItems(service, token, subject, description, form, resourcesToAssignTo);

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
