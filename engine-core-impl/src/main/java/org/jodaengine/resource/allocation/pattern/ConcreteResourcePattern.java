package org.jodaengine.resource.allocation.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.allocation.Form;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;


/**
 * 
 */
public class ConcreteResourcePattern implements CreationPattern {

    private String subject;
    
    private String description;
    
    private Form form;
    
    private AbstractResource<?>[] resourcesToAssignTo;
    
    /**
     * Instantiates a new concrete resource pattern. The parameters are used for the creation of the {@link AbstractWorklistItem}s.
     *
     * @param subject the subject
     * @param description the description
     * @param form the form
     * @param assignedResources the assigned resources
     */
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
