package de.hpi.oryxengine.resource.worklist;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;

/**
 * THe implementation of the Task Interface.
 */
public class WorklistItemImpl extends AbstractWorklistItem {

    private WorklistItemState status;
    private Task task;
    private transient Token correspondingToken;
    private UUID id;
    
    /**
     * Hidden constructor for deserialization.
     */
    protected WorklistItemImpl() { }
    
    /**
     * Default Constructor.
     *
     * @param task the {@link Task} that should be executed
     * @param correspondingToken the corresponding {@link Token} of the task
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
        this.id = UUID.randomUUID();
    }

    @Override
    @JsonIgnore
    public String getSubject() {

        return task.getSubject();
    }

    @Override
    @JsonIgnore
    public String getDescription() {

        return task.getDescription();
    }

    @Override
    @JsonIgnore
    public Form getForm() {

        return task.getForm();
    }

    @Override
    @JsonIgnore
    public AllocationStrategies getAllocationStrategies() {

        return task.getAllocationStrategies();
    }

    @Override
    @JsonProperty
    public Set<AbstractResource<?>> getAssignedResources() {

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
     *            - a {@link AbstractWorklistItem} object
     * @return worklistItemImpl - the casted {@link WorklistItemImpl} object
     *             - an {@link JodaEngineRuntimeException} if the provided Parameter is null
     */
    public static WorklistItemImpl asWorklistItemImpl(AbstractWorklistItem worklistItem) {

        if (worklistItem == null) {
            throw new JodaEngineRuntimeException("The WorklistItem parameter is null.");
        }
        WorklistItemImpl worklistItemImpl = (WorklistItemImpl) worklistItem;

        return worklistItemImpl;
    }

    @Override
    public UUID getID() {
        return this.id;
    }
    
    /**
     * Gets the underlying task.
     *
     * @return the task
     */
    @JsonProperty
    public Task getTask() {
        return this.task;
    }

    /**
     * Sets any other type not recognized by JSON serializer.
     * This method is indented to be used by Jackson.
     * 
     * @param fieldName
     *            - the fieldName
     * @param value
     *            - the value
     */
    @JsonAnySetter
    protected void setOtherJson(@Nonnull String fieldName,
                                @Nullable String value) {
        
        if ("id".equals(fieldName)) {
            this.id = UUID.fromString(value);
        }
    }
    
    /**
     * Sets the underlying task. This method is ONLY used for Jackson.
     *
     * @param task - the task to be set
     */
    @JsonProperty
    protected void setTask(@Nonnull Task task) {
        this.task = task;
    }
}
