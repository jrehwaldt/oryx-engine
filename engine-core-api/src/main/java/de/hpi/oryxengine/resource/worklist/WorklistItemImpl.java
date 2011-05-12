package de.hpi.oryxengine.resource.worklist;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;

/**
 * THe implementation of the Task Interface.
 */
public class WorklistItemImpl extends AbstractWorklistItem {

    private WorklistItemState status;
    private transient Token correspondingToken;
    private UUID id;
    private String subject, description;
    private Form form;
    private Set<AbstractResource<?>> assignedResources;
    
    /**
     * Hidden constructor for deserialization.
     */
    protected WorklistItemImpl() { }
    
    /**
     * Default Constructor.
     *
     * @param description the description
     * @param subject the subject of the item
     * @param form the form
     * @param assignedResources the assigned resources
     * @param correspondingToken the corresponding {@link Token} of the task
     */
    public WorklistItemImpl(String subject,
                            String description,                            
                            Form form,
                            Set<AbstractResource<?>> assignedResources,
                            @Nonnull Token correspondingToken) {

        if (correspondingToken == null) {
            throw new NullPointerException("The corresponding Token parameter cannot be null.");
        }
        
        this.subject = subject;
        this.description = description;
        this.form = form;
        this.assignedResources = assignedResources;
        this.status = WorklistItemState.OFFERED;
        this.correspondingToken = correspondingToken;
        this.id = UUID.randomUUID();
    }

    @Override
    @JsonIgnore
    public String getSubject() {

        return subject;
    }

    @Override
    @JsonIgnore
    public String getDescription() {

        return description;
    }

    @Override
    @JsonIgnore
    public Form getForm() {

        return form;
    }

    @Override
    @JsonProperty
    public Set<AbstractResource<?>> getAssignedResources() {

        return assignedResources;
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
     * Sets the subject. This method is ONLY used for Jackson.
     *
     * @param subject - the subject to be set
     */
    @JsonProperty
    protected void setSubject(@Nonnull String subject) {
        this.subject = subject;
    }
    
    /**
     * Sets the description. This method is ONLY used for Jackson.
     *
     * @param description - the description to be set
     */
    @JsonProperty
    protected void setDescription(@Nonnull String description) {
        this.description = description;
    }
}
