package org.jodaengine.resource.allocation;

import org.jodaengine.resource.AbstractResource;

/**
 * This class helps to build {@link WorklistItem Worklist Items}.
 * One can set description, subject etc. and build multiple Patterns with the same information.
 */
public interface CreationPatternBuilder {

    /**
     * Sets the subject of the {@link WorklistItem} that should be built.
     * 
     * @param subject
     *            - the subject of the Worklist items to create
     * @return the current {@link CreationPatternBuilder} in order continue configuring the creation pattern
     */
    CreationPatternBuilder setItemSubject(String subject);

    /**
     * Sets the description of the {@link WorklistItem} that should be built.
     * 
     * @param taskDescription
     *            - the description of the {@link WorklistItem}
     * @return the current {@link CreationPatternBuilder} in order continue configuring the creation pattern
     */
    CreationPatternBuilder setItemDescription(String taskDescription);

    /**
     * Sets the {@link Form} of the {@link WorklistItem} that should be built.
     * 
     * @param formID
     *            the id of the process artifact that represents this form
     * @return the current {@link CreationPatternBuilder} in order continue configuring the creation pattern
     */
    CreationPatternBuilder setItemFormID(String formID);

    /**
     * Adds an {@link AbstractResource Resource} that should be assigned to the {@link WorklistItem}.
     * 
     * @param resourceAssignedToItem
     *            - the {@link AbstractResource Resource} that should be assigned to the {@link WorklistItem}
     * @return the current {@link CreationPatternBuilder} in order continue configuring the creation pattern
     */
    CreationPatternBuilder addResourceAssignedToItem(AbstractResource<?> resourceAssignedToItem);

    /**
     * Removes all currently assigned resources, so that the pattern builder can be reused.
     * 
     * @return the creation pattern builder
     */
    CreationPatternBuilder flushAssignedResources();

    /**
     * Builds a concrete resource pattern out of the given specification. For other Patterns, you would extend this
     * interface.
     * 
     * @param creationPatternClass
     *            the specific creation pattern class that implements the {@link CreationPattern Creation Pattern
     *            Interface}
     * @return the {@link AbstractCreationPattern} specified before by the {@link CreationPatternBuilder}
     */
    CreationPattern buildCreationPattern(Class<? extends CreationPattern> creationPatternClass);
}
