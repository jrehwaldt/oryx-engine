package de.hpi.oryxengine.worklist;

import java.util.Set;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.Resource;

/**
 * THe implementation of the Task Interface.
 */
public class WorklistItemImpl implements WorklistItem {

    private WorklistItemState status;
    private Task task;
    private Token correspondingToken;
    
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
     * @param role
     *            - a {@link WorklistItem} object
     * @return roleImpl - the casted {@link WorklistItemImpl} object
     * @throws DalmatinaException
     *             - an {@link DalmatinaException} if the provided Parameter is null
     */
    public static WorklistItemImpl asWorklistItemImpl(WorklistItem worklistItem)
    throws DalmatinaException {

        if (worklistItem == null) {
            throw new DalmatinaException("The WorklistItem parameter is null.");
        }
        WorklistItemImpl worklistItemImpl = (WorklistItemImpl) worklistItem;

        return worklistItemImpl;
    }
}
