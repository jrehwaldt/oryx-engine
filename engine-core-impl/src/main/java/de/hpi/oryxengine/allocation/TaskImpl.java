package de.hpi.oryxengine.allocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.resource.AbstractResource;

/**
 * THe implementation of the Task Interface.
 */
public class TaskImpl implements Task {

    private String subject;
    
    private String description;
    
    private AllocationStrategies allocationStrategies;
    private Set<AbstractResource<?>> assignedResources;

    /**
     * Default Constructor.
     * 
     * @param subject
     *            - the subject of the task
     * @param description
     *            - further information for the task
     * @param allocationStrategies
     *            - constains all {@link AllocationStrategies} responsible for the distribution of the task
     * @param set
     *            - a list of {@link AbstractResource}s that should be assigned to a this task
     */
    public TaskImpl(String subject,
                    String description,
                    AllocationStrategies allocationStrategies,
                    Set<AbstractResource<?>> set) {

        this.subject = subject;
        this.description = description;
        this.allocationStrategies = allocationStrategies;
        this.assignedResources = set;
    }

    /**
     * Default Constructor.
     * 
     * @param subject
     *            - the subject of the task
     * @param description
     *            - further information for the task
     * @param allocationStrategies
     *            - constains all {@link AllocationStrategies} responsible for the distribution of the task
     * @param assignedResource
     *            - the assigned {@link AbstractResource} that should be assigned to a this task
     */
    @SuppressWarnings("unchecked")
    public TaskImpl(String subject,
                    String description,
                    AllocationStrategies allocationStrategies,
                    AbstractResource<?> assignedResource) {

        this(subject,
            description,
            allocationStrategies,
            new HashSet<AbstractResource<?>>(Arrays.asList(assignedResource)));
    }

    @Override
    public String getSubject() {

        return subject;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public Form getForm() {

        return null;
    }

    @Override
    public AllocationStrategies getAllocationStrategies() {

        return allocationStrategies;
    }

    @Override
    public Set<AbstractResource<?>> getAssignedResources() {

        if (assignedResources == null) {
            assignedResources = new HashSet<AbstractResource<?>>();
        }
        return assignedResources;
    }

}
