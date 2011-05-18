package org.jodaengine.resource.worklist;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

import org.jodaengine.allocation.Form;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;

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
    protected WorklistItemImpl() {

    }

    /**
     * Default Constructor.
     * 
     * @param description
     *            the description
     * @param subject
     *            the subject of the item
     * @param form
     *            the form
     * @param assignedResources
     *            the assigned resources
     * @param correspondingToken
     *            the corresponding {@link Token} of the task
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
        this.correspondingToken = correspondingToken;
        this.id = UUID.randomUUID();
        this.setStatus(WorklistItemState.CREATED);
    }

    @Override
    public String getSubject() {

        return subject;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public Form getForm() {

        return form;
    }

    @Override
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
    @JsonProperty
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
     *         - an {@link JodaEngineRuntimeException} if the provided Parameter is null
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
    protected void setOtherJson(@Nonnull String fieldName, @Nullable String value) {

        if ("id".equals(fieldName)) {
            this.id = UUID.fromString(value);
        }
    }

    /**
     * Sets the subject. This method is ONLY used for Jackson.
     * 
     * @param subject
     *            - the subject to be set
     */
    @JsonProperty
    protected void setSubject(@Nonnull String subject) {

        this.subject = subject;
    }

    /**
     * Sets the description. This method is ONLY used for Jackson.
     * 
     * @param description
     *            - the description to be set
     */
    @JsonProperty
    protected void setDescription(@Nonnull String description) {

        this.description = description;
    }
}
