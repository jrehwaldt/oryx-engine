package de.hpi.oryxengine.allocation;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.resource.worklist.WorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * The implementation of the Task Interface.
 */
@XmlRootElement
public class WorklistItemImpl implements WorklistItem {

    private WorklistItemState status;
    private Task task;
    private Token correspondingToken;
    
    /**
     * Default constructor.
     * 
     * @param task the task
     * @param correspondingToken the instance's token
     */
    public WorklistItemImpl(@Nonnull Task task,
                            @Nonnull Token correspondingToken) {

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
     * @param worklistItem
     *            - a {@link WorklistItem} object
     * @return worklistItemImpl - the casted {@link WorklistItemImpl} object
     */
    public static WorklistItemImpl asWorklistItemImpl(WorklistItem worklistItem) {

        if (worklistItem == null) {
            throw new DalmatinaRuntimeException("The WorklistItem parameter is null.");
        }
        WorklistItemImpl worklistItemImpl = (WorklistItemImpl) worklistItem;

        return worklistItemImpl;
    }
}
