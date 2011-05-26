package org.jodaengine.resource.allocation.pattern.creation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jodaengine.RepositoryService;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.CreationPattern;
import org.jodaengine.resource.allocation.PushPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;

/**
 * 
 */
public abstract class AbstractCreationPattern implements CreationPattern {

    protected String subject;

    protected String description;

    protected String formID;

    protected List<AbstractResource<?>> resourcesToAssignTo;

    protected PushPattern pushPattern;

    /**
     * Instantiates a new concrete resource pattern. The parameters are used for the creation of the worklist item and
     * its distribution.
     * 
     * @param subject
     *            the subject
     * @param description
     *            the description
     * @param formID
     *            the id of the process artifact, that contains this form
     * @param assignedResources
     *            the assigned resources {@link AbstractWorklistItem}s.
     */
    @SuppressWarnings("unchecked")
    public AbstractCreationPattern(String subject,
                                   String description,
                                   String formID,
                                   List<?> assignedResources) {
        this.subject = subject;
        this.description = description;
        this.formID = formID;
        this.resourcesToAssignTo = (List<AbstractResource<?>>) assignedResources;
        setPushPattern();
    }

    /**
     * Sets the push pattern according to the Pattern, every Creation Pattern has to do this.
     */
    protected abstract void setPushPattern();

    /**
     * Convenience constructor for only one resource.
     * 
     * @param subject
     *            the subject
     * @param description
     *            the description
     * @param formID
     *            the form id
     * @param assignedResource
     *            the assigned resource
     */
    @SuppressWarnings("unchecked")
    public AbstractCreationPattern(String subject,
                                   String description,
                                   String formID,
                                   AbstractResource<?> assignedResource) {
        
        this(subject, description, formID, Arrays.asList(assignedResource));
    }

    @Override
    public abstract AbstractWorklistItem createWorklistItem(Token token, RepositoryService repoService);

    /**
     * Gets the assigned resources of the worklist items. Method is for test purposes only.
     * 
     * @return the assigned resources
     */
    public Set<AbstractResource<?>> getAssignedResources() {

        return new HashSet<AbstractResource<?>>(resourcesToAssignTo);
    }

    /**
     * Gets the item's subject. Method is for test purposes only.
     * 
     * @return the item subject
     */
    public String getItemSubject() {

        return subject;
    }

    /**
     * Gets the item's description. Method is for test purposes only.
     * 
     * @return the item description
     */
    public String getItemDescription() {

        return description;
    }

    /**
     * Gets the item's form. Method is for test purposes only.
     * 
     * @return the item form
     */
    public String getItemFormID() {

        return formID;
    }
    
    @Override
    public PushPattern getPushPattern() {

        return pushPattern;
    }

}
