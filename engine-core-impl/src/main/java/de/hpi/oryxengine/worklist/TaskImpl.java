package de.hpi.oryxengine.worklist;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.resource.Resource;

/**
 * THe implementation of the Task Interface.
 */
public class TaskImpl implements Task {

    /** The subject. */
    private String subject;
    
    /** The description. */
    private String description;
    
    /** The allocation strategies. */
    private AllocationStrategies allocationStrategies;
    private Set<Resource<?>> assignedResources;

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
     *            - a list of {@link Resource}s that should be assigned to a this task
     */
    public TaskImpl(String subject,
                    String description,
                    AllocationStrategies allocationStrategies,
                    Set<Resource<?>> set) {

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
     *            - the assigned {@link Resource} that should be assigned to a this task
     */
    @SuppressWarnings("unchecked")
    public TaskImpl(String subject,
                    String description,
                    AllocationStrategies allocationStrategies,
                    Resource<?> assignedResource) {

        this(subject,
            description,
            allocationStrategies,
            new HashSet<Resource<?>>(Arrays.asList(assignedResource)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubject() {

        return subject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {

        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Form getForm() {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AllocationStrategies getAllocationStrategies() {

        return allocationStrategies;
    }

    @Override
    public Set<Resource<?>> getAssignedResources() {

        if (assignedResources == null) {
            assignedResources = new HashSet<Resource<?>>();
        }
        return assignedResources;
    }

}
