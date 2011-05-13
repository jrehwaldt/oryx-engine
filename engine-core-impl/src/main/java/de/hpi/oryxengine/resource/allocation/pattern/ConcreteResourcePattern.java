package de.hpi.oryxengine.resource.allocation.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * 
 */
public class ConcreteResourcePattern implements CreationPattern {

    private String subject;
    
    private String description;
    
    private Form form;
    
    private AbstractResource<?>[] resourcesToAssignTo;
    
    public ConcreteResourcePattern(String subject,
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
    public ConcreteResourcePattern(String subject,
                                   String description,
                                   Form form,
                                   AbstractResource<?> assignedResource) {
      this(subject, description, form, new AbstractResource<?>[] {assignedResource});
  }

    @Override
    public List<AbstractWorklistItem> createWorklistItems(Token token) {

        List<AbstractWorklistItem> itemsToDistribute = new ArrayList<AbstractWorklistItem>();
        Set<AbstractResource<?>> assignedResourcesCopy = new HashSet<AbstractResource<?>>(
        Arrays.asList(resourcesToAssignTo));
        WorklistItemImpl worklistItem = new WorklistItemImpl(subject, description, form, assignedResourcesCopy, token);
        itemsToDistribute.add(worklistItem);
    
        return itemsToDistribute;
        
    }
    
    @Override
    public AbstractResource<?>[] getAssignedResources() {

        return resourcesToAssignTo;
    }

    @Override
    public String getItemSubject() {

        return subject;
    }

    @Override
    public String getItemDescription() {

        return description;
    }

    @Override
    public Form getItemForm() {

        return form;
    }

}
