package org.jodaengine.resource.allocation.pattern;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jodaengine.RepositoryService;
import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.allocation.Form;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.FormImpl;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class ConcreteResourcePattern implements CreationPattern {

    private String subject;

    private String description;

    private String formID;

    private AbstractResource<?>[] resourcesToAssignTo;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new concrete resource pattern. The parameters are used for the creation of the
     *
     * @param subject the subject
     * @param description the description
     * @param formID the id of the process artifact, that contains this form
     * @param assignedResources the assigned resources
     * {@link AbstractWorklistItem}s.
     */
    public ConcreteResourcePattern(String subject,
                                   String description,
                                   String formID,
                                   AbstractResource<?>[] assignedResources) {

        this.subject = subject;
        this.description = description;
        this.formID = formID;
        this.resourcesToAssignTo = assignedResources;
    }

    /**
     * Convenience constructor.
     *
     * @param subject the subject
     * @param description the description
     * @param formID the form id
     * @param assignedResource the assigned resource
     */
    public ConcreteResourcePattern(String subject, 
                                   String description, 
                                   String formID, 
                                   AbstractResource<?> assignedResource) {

        this(subject, description, formID, new AbstractResource<?>[] {assignedResource});
    }

    @Override
    public AbstractWorklistItem createWorklistItem(Token token, RepositoryService repoService) {

        Set<AbstractResource<?>> assignedResourcesCopy = new HashSet<AbstractResource<?>>(
            Arrays.asList(resourcesToAssignTo));        
        
        Form formToUse = null;
        
        if(formID != null) {
            // only search for a form, if one has been specified
            try {
                ProcessDefinitionID definitionID = token.getInstance().getDefinition().getID();
                formToUse = new FormImpl(repoService.getProcessArtifact(formID, definitionID));
            } catch (ProcessArtifactNotFoundException e) {
                // TODO @Thorben-Refactoring refactor the error handling here.
                logger.error("The requested form does not exist.");
            }
        }
        
        WorklistItemImpl worklistItem = new WorklistItemImpl(subject, description, formToUse, assignedResourcesCopy, token);
        return worklistItem;

    }

    /**
     * Gets the assigned resources of the worklist items. Method is for test purposes only.
     * 
     * @return the assigned resources
     */
    public Set<AbstractResource<?>> getAssignedResources() {

        return new HashSet<AbstractResource<?>>(Arrays.asList(resourcesToAssignTo));
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

}
