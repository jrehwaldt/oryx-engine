package de.hpi.oryxengine.allocation;

import java.util.Set;

import de.hpi.oryxengine.resource.AbstractResource;


/**
 * Represents a human task for engine internal use.
 */
public interface Task {
    
    /**
     * Returns the subject of the task.
     * 
     * @return String representing the subject
     */
    String getSubject();
    
    /**
     * Returns the description of the task.
     * 
     * @return String representing the description
     */
    String getDescription();
    
    /**
     * Returns a form object for user input.
     * 
     * @return the form that 
     */
    Form getForm();
    
    /**
     * Retrieves the {@link AllocationStrategies} for the {@link Task}.
     * 
     * @return the {@link AllocationStrategies} corresponding to this {@link Task}
     */
    AllocationStrategies getAllocationStrategies();
    
    /**
     * Retrieves the {@link AbstractResource}s that is assigned to this task.
     * 
     * @return a list of {@link AbstractResource}s that is assigned to this task
     */
    Set<AbstractResource<?>> getAssignedResources();
}
