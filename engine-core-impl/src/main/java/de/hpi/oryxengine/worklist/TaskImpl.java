package de.hpi.oryxengine.worklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hpi.oryxengine.resource.Resource;

/**
 * THe implementation of the Task Interface.
 */
public class TaskImpl implements Task {

    private String subject;
    private String description;
    private AllocationStrategies allocationStrategies;
    private List<Resource<?>> assignedResources;

    /**
     * Default Constructor.
     * 
     * @param subject
     *            - the subject of the task
     * @param description
     *            - further information for the task
     * @param allocationStrategies
     *            - constains all {@link AllocationStrategies} responsible for the distribution of the task
     * @param list
     *            - a list of {@link Resource}s that should be assigned to a this task
     */
    public TaskImpl(String subject,
                    String description,
                    AllocationStrategies allocationStrategies,
                    List<Resource<?>> list) {

        this.subject = subject;
        this.description = description;
        this.allocationStrategies = allocationStrategies;
        this.assignedResources = list;
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
            new ArrayList<Resource<?>>(Arrays.asList(assignedResource)));
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

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AllocationStrategies getAllocationStrategies() {

        return allocationStrategies;
    }

    @Override
    public List<Resource<?>> getAssignedResources() {

        if (assignedResources == null) {
            assignedResources = new ArrayList<Resource<?>>();
        }
        return assignedResources;
    }

}
