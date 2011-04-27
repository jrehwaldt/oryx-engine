package de.hpi.oryxengine.allocation;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import de.hpi.oryxengine.resource.AbstractResource;


/**
 * Represents a human task for engine internal use.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface Task {
    
    /**
     * Returns the subject of the task.
     * 
     * @return String representing the subject
     */
    @JsonProperty
    String getSubject();
    
    /**
     * Returns the description of the task.
     * 
     * @return String representing the description
     */
    @JsonProperty
    String getDescription();
    
    /**
     * Returns a form object for user input.
     * 
     * @return the form that 
     */
    @JsonIgnore
    Form getForm();
    
    /**
     * Retrieves the {@link AllocationStrategies} for the {@link Task}.
     * 
     * @return the {@link AllocationStrategies} corresponding to this {@link Task}
     */
    @JsonIgnore
    AllocationStrategies getAllocationStrategies();
    
    /**
     * Retrieves the {@link AbstractResource}s that is assigned to this task.
     * 
     * @return a set of {@link AbstractResource}s that is assigned to this task
     */
    @JsonIgnore
    Set<AbstractResource<?>> getAssignedResources();
}
