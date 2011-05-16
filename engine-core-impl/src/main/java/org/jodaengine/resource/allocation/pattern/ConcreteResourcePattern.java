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
     * Instantiates a new concrete resource pattern. 
     * The parameters are used for the creation of the {@link AbstractWorklistItem}s.
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
    
    /**
     * Gets the assigned resources of the worklist items.
     *
     * @return the assigned resources
     */
    @Override
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
    public Form getItemForm() {

        return form;
    }

}
