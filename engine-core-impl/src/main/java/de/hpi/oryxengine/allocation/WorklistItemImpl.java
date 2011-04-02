package de.hpi.oryxengine.allocation;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.resource.worklist.WorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * THe implementation of the Task Interface.
 */
public class WorklistItemImpl implements WorklistItem {

    private WorklistItemState status;
    private Task task;
    private Token correspondingToken;
    private UUID id;
    
    /**
     * Default Constructor.
     *
     * @param task the {@link Task} that should be executed
     * @param correspondingToken the corresponding {@link Token} of the task
     */
    public WorklistItemImpl(@Nonnull Task task, @Nonnull Token correspondingToken) {

        if (task == null) {
            throw new NullPointerException("The Task parameter cannot be null.");
        }

        if (correspondingToken == null) {
            throw new NullPointerException("The corresponding Token parameter cannot be null.");
        }
        
        this.task = task;
        this.status = WorklistItemState.OFFERED;
        this.correspondingToken = correspondingToken;
        this.id = UUID.randomUUID();
    }

    @Override
    public String getSubject() {

        return task.getSubject();
    }

    @Override
    public String getDescription() {

        return task.getDescription();
    }

    @Override
    public Form getForm() {

        return task.getForm();
    }

    @Override
    public AllocationStrategies getAllocationStrategies() {

        return task.getAllocationStrategies();
    }

    @Override
    public Set<Resource<?>> getAssignedResources() {

        return task.getAssignedResources();
    }

    @Override
    public WorklistItemState getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * 
     * @param status
     *            the new {@link WorklistItemState}
     */
    public void setStatus(WorklistItemState status) {

        this.status = status;
    }

    @Override
    public Token getCorrespondingToken() {

        return correspondingToken;
    }
    
    /**
     * Translates a WorklistItem into a corresponding WorklistItemImpl object.
     * 
     * @param worklistItem
     *            - a {@link WorklistItem} object
     * @return worklistItemImpl - the casted {@link WorklistItemImpl} object
     *             - an {@link DalmatinaRuntimeException} if the provided Parameter is null
     */
    public static WorklistItemImpl asWorklistItemImpl(WorklistItem worklistItem) {

        if (worklistItem == null) {
            throw new DalmatinaRuntimeException("The WorklistItem parameter is null.");
        }
        WorklistItemImpl worklistItemImpl = (WorklistItemImpl) worklistItem;

        return worklistItemImpl;
    }

    @Override
    public UUID getID() {
        return this.id;
    }
}
