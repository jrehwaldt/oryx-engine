package org.jodaengine.resource.allocation.pattern.creation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.Form;
import org.jodaengine.resource.allocation.pattern.push.AllocateSinglePattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;

/**
 * The Class DirectDistributionPattern.
 */
public class DirectDistributionPattern extends AbstractCreationPattern {

    /**
     * Default Constructor that instantiates a new direct distribution pattern. <br/>
     * see also {@link AbstractCreationPattern#AbstractCreationPattern(String, String, String, AbstractResource[])}
     * 
     * @param subject
     *            the subject
     * @param description
     *            the description
     * @param formID
     *            the form id
     * @param assignedResources
     *            the assigned resources
     */
    public DirectDistributionPattern(String subject,
                                     String description,
                                     String formID,
                                     List<AbstractResource<?>> assignedResources) {

        super(subject, description, formID, assignedResources);
    }

    /**
     * Convenience Constructor. <br/>
     * see also {@link #DirectDistributionPattern(String, String, String, AbstractResource[])}
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
    public DirectDistributionPattern(String subject,
                                     String description,
                                     String formID,
                                     AbstractResource<?> assignedResource) {

        super(subject, description, formID, assignedResource);
    }

    @Override
    protected void setPushPattern() {

        this.pushPattern = new AllocateSinglePattern();
    }

    @Override
    public AbstractWorklistItem createWorklistItem(Token token, RepositoryService repoService) {

        Set<AbstractResource<?>> assignedResourcesCopy = new HashSet<AbstractResource<?>>(resourcesToAssignTo);

        Form formToUse = null;

        if (formID != null) {
            // only search for a form, if one has been specified
            try {
                ProcessDefinitionID definitionID = token.getInstance().getDefinition().getID();
                formToUse = repoService.getForm(formID, definitionID);
            } catch (ProcessArtifactNotFoundException e) {
                throw new JodaEngineRuntimeException("The requested form does not exist.", e);
            }
        }

        WorklistItemImpl worklistItem = new WorklistItemImpl(subject, description, formToUse, assignedResourcesCopy,
            token);
        return worklistItem;

    }
}
