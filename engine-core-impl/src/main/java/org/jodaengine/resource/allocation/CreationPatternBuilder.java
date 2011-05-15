package org.jodaengine.resource.allocation;

import org.jodaengine.allocation.Form;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;

/**
 * This class helps to build {@link WorklistItem WorklistItems}.
 */
public interface CreationPatternBuilder {

    /**
     * Sets the subject of the {@link WorklistItem} that should be built.
     * 
     * @param subject
     *            - the subject of the Task
     * @return the current {@link CreationPatternBuilder} in order continue configuring the Task
     */
    CreationPatternBuilder setItemSubject(String subject);

    /**
     * Sets the description of the {@link WorklistItem} that should be built.
     * 
     * @param taskDescription
     *            - the description of the {@link WorklistItem}
     * @return the current {@link CreationPatternBuilder} in order continue configuring the Task
     */
    CreationPatternBuilder setItemDescription(String taskDescription);

    /**
     * Sets the {@link Form} of the {@link WorklistItem} that should be built.
     * 
     * @param form
     *            - the {@link Form} of the {@link WorklistItem}
     * @return the current {@link CreationPatternBuilder} in order continue configuring the Task
     */
    CreationPatternBuilder setItemForm(Form form);

    /**
     * Adds an {@link AbstractResource Resource} that should be assigned to the {@link WorklistItem}.
     * 
     * @param resourceAssignedToItem
     *            - the {@link AbstractResource Resource} that should be assigned to the {@link WorklistItem}
     * @return the current {@link CreationPatternBuilder} in order continue configuring the Task
     */
    CreationPatternBuilder addResourceAssignedToItem(AbstractResource<?> resourceAssignedToItem);
    
    /**
     * Removes all currently assigned resources, so that the pattern builder can be reused.
     *
     * @return the creation pattern builder
     */
    CreationPatternBuilder flushAssignedResources();

    /**
     * Builds the task.
     * 
     * @return the {@link Task} specified before by the {@link CreationPatternBuilder}
     */
    ConcreteResourcePattern buildConcreteResourcePattern();
}
