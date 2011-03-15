package de.hpi.oryxengine.worklist;

import java.util.List;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.resource.Resource;

/**
 * THe implementation of the Task Interface.
 */
public class WorklistItemImpl implements WorklistItem {

    private WorklistItemState status;
    private Task task;

    public WorklistItemImpl(@Nonnull Task task) {

        this.task = task;
        this.status = WorklistItemState.OFFERED;
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
}
