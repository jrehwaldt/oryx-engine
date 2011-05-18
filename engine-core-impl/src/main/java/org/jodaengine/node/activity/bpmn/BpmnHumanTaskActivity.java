package org.jodaengine.node.activity.bpmn;

import java.util.ArrayList;
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
    
    @JsonIgnore
    private static final String ITEM_PREFIX = "ITEMS-";

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
        AbstractWorklistItem item = creationPattern.createWorklistItem(token);

        // save the UUIDs of the created items to the instance context, in order to be able to delete them, if execution
        // is canceled
        List<UUID> itemUUIDs = new ArrayList<UUID>();
        itemUUIDs.add(item.getID());

        ProcessInstanceContext context = token.getInstance().getContext();
        
        // the name should be unique, as the token can only work on one activity at a time.
        final String itemContextVariableIdentifier = ITEM_PREFIX + token.getID();
        context.setInternalVariable(itemContextVariableIdentifier, itemUUIDs);

        pushPattern.distributeWorkitem(service, item);

        token.suspend();
    }

    @Override
    public void cancel(Token token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        final String itemContextVariableIdentifier = ITEM_PREFIX + token.getID();
        List<UUID> itemUUIDs = (List<UUID>) context.getInternalVariable(itemContextVariableIdentifier);

        for (UUID itemUUID : itemUUIDs) {
            ServiceFactory.getWorklistQueue().removeWorklistItem(itemUUID);
        }

    }

    @Override
    public void resume(Token token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        final String itemContextVariableIdentifier = ITEM_PREFIX + token.getID();
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
