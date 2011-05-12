package de.hpi.oryxengine.resource.allocation.pattern;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * 
 */
public class RoleDistributionPattern implements CreationPattern {

    private String subject;
    
    private String description;
    
    private Form form;
    
    private AbstractResource<?>[] resourcesToAssignTo;
    
    public RoleDistributionPattern(String subject,
                                     String description,
                                     Form form,
                                     AbstractResource<?>[] assignedResources) {
        this.subject = subject;
        this.description = description;
        this.form = form;
        this.resourcesToAssignTo = assignedResources;
    }
    
    /**
     * Convenience constructor.
     *
     * @param subject the subject
     * @param description the description
     * @param form the form
     * @param assignedResource the assigned resource
     */
    public RoleDistributionPattern(String subject,
                                   String description,
                                   Form form,
                                   AbstractResource<?> assignedResource) {
      this(subject, description, form, new AbstractResource<?>[] {assignedResource});
  }

    @Override
    public void createWorklistItems(TaskAllocation worklistService, Token token) {

        Set<AbstractResource<?>> assignedResourcesCopy = new HashSet<AbstractResource<?>>(
        Arrays.asList(resourcesToAssignTo));
        WorklistItemImpl worklistItem = new WorklistItemImpl(subject, description, form, assignedResourcesCopy, token);
        worklistItem.setStatus(WorklistItemState.OFFERED);
    
        // TODO @Thorben-Refactoring do we need new worklist items for every worker?
        worklistService.addWorklistItem(worklistItem, assignedResourcesCopy);
        
    }
    
    @Override
    public AbstractResource<?>[] getAssignedResources() {

        return resourcesToAssignTo;
    }

}
