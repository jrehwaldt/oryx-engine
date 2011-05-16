package org.jodaengine.node.activity.bpmn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.ServiceFactory;
import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.allocation.PushPattern;
import org.jodaengine.allocation.TaskAllocation;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;

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

    @JsonIgnore
    private final String itemContextVariableIdentifier = "ITEMS-" + this.toString();

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

        // save the UUIDs of the created items to the instance context, in order to be able to delete them, if execution
        // is cancelled
        List<UUID> itemUUIDs = new ArrayList<UUID>();
        for (AbstractWorklistItem item : items) {
            itemUUIDs.add(item.getID());
        }

        ProcessInstanceContext context = token.getInstance().getContext();
        setItemContextVariable(itemUUIDs, context);

        pushPattern.distributeWorkitems(service, items);

        token.suspend();
    }

    /**
     * Sets the context variable that stores the currently executing worklist items for this activity. It is
     * synchronized, as the set-Process should not be carried out simultaneously due to concurrency issues.
     * 
     * @param itemUUIDs
     *            the item uui ds
     * @param context
     *            the context
     */
    private synchronized void setItemContextVariable(List<UUID> itemUUIDs, ProcessInstanceContext context) {

        // it may be possible that this activity is executed more than once simultaniously, there might be more
        // in-work-items associated with this activity.

        if (context.getInternalVariable(itemContextVariableIdentifier) == null) {
            context.setInternalVariable(itemContextVariableIdentifier, itemUUIDs);
        } else {
            List<UUID> currentlyExecutingItems = (List<UUID>) context
            .getInternalVariable(itemContextVariableIdentifier);
            currentlyExecutingItems.addAll(itemUUIDs);
        }
    }

    @Override
    public void cancel(Token token) {

        // for (AbstractResource<?> resource : creationPattern.getAssignedResources()) {
        // Iterator<AbstractWorklistItem> it = resource.getWorklist().iterator();
        //
        // while (it.hasNext()) {
        // WorklistItemImpl item = (WorklistItemImpl) it.next();
        // ServiceFactory.getWorklistQueue().removeWorklistItem(item, resource);
        // }
        // }

        ProcessInstanceContext context = token.getInstance().getContext();
        List<UUID> itemUUIDs = (List<UUID>) context.getInternalVariable(itemContextVariableIdentifier);

        for (UUID itemUUID : itemUUIDs) {
            // TODO remove the worklist item with the given id
        }

    }

    @Override
    public void resume(Token token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        context.deleteInternalVariable(itemContextVariableIdentifier);
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
