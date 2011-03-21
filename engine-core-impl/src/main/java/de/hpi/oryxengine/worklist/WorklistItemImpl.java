package de.hpi.oryxengine.worklist;

import java.util.List;

import javax.annotation.Nonnull;

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
    public List<Resource<?>> getAssignedResources() {

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
}
