package de.hpi.oryxengine.allocation;

import javax.annotation.Nonnull;

/**
 * The Interface for the container that retrieves all the {@link Pattern}s of a task.
 */
public interface AllocationStrategies {

    /**
     * Gets the pull pattern of the task.
     * 
     * Pull patterns describe the way a task can be allocated by resources and 
     * at what time they are executed by them. They also define what happens to 
     * the task in other worklists, e.g. deletion from all other lists.
     * 
     * The method must not return null. This pattern must be available for the distribution of {@link Task}s.
     * 
     * web reference:
     * http://www.workflowpatterns.com/patterns/resource/#Pull
     *
     * @return the pull pattern
     */
    @Nonnull Pattern getPullPattern();
    
    /**
     * Gets the push pattern for the task.
     * 
     * Push patterns describe the way tasks are offered or allocated to resources.
     * 
     * The method must not return null. This pattern must be available for the distribution of {@link Task}s.
     * 
     * web reference:
     * http://www.workflowpatterns.com/patterns/resource/#Push
     *
     * @return the push pattern
     */
    @Nonnull Pattern getPushPattern();
    
    /**
     * Gets the detour pattern for the task.
     * 
     * Detour patterns describe the consequences of interruptions on task distributions
     * that can be either system or resource based.
     * 
     * web reference:
     * http://www.workflowpatterns.com/patterns/resource/#Detour
     *
     * @return the detour pattern
     */
    Pattern getDetourPattern();  
}

