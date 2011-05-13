package de.hpi.oryxengine.resource.allocation;

import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.allocation.pattern.ConcreteResourcePattern;
import de.hpi.oryxengine.resource.allocation.pattern.ConcreteResourcePattern;

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
     * Builds the task.
     * 
     * @return the {@link Task} specified before by the {@link CreationPatternBuilder}
     */
    ConcreteResourcePattern buildConcreteResourcePattern();
}
