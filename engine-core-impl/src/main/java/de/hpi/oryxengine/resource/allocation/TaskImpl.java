package de.hpi.oryxengine.resource.allocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonAnySetter;

import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.resource.AbstractResource;

/**
 * THe implementation of the Task Interface.
 */
public class TaskImpl implements Task {

    private String subject;

    private String description;

    private transient AllocationStrategies allocationStrategies;
    private transient Set<AbstractResource<?>> assignedResources;

    /**
     * Hidden constructor for deserialization.
     */
    protected TaskImpl() {

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
     * Constructor for this class. It can be used when the {@link Task} is assigned to only one {@link AbstractResource
     * Resource}.
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

        this(subject, description, allocationStrategies, new HashSet<AbstractResource<?>>(
            Arrays.asList(assignedResource)));
    }

    /**
     * Copy constructor.
     * 
     * @param taskToCopy
     *            the task to copy
     */
    public TaskImpl(Task taskToCopy) {

        this.subject = taskToCopy.getSubject();
        this.description = taskToCopy.getDescription();
        this.allocationStrategies = taskToCopy.getAllocationStrategies();
        HashSet<AbstractResource<?>> setCopy = new HashSet<AbstractResource<?>>(taskToCopy.getAssignedResources());
        this.assignedResources = setCopy;

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

    /**
     * Sets any other type not recognized by JSON serializer. This method is indented to be used by Jackson.
     * 
     * @param fieldName
     *            - the fieldName
     * @param value
     *            - the value
     */
    @JsonAnySetter
    protected void setOtherJson(@Nonnull String fieldName, @Nullable String value) {

        if ("subject".equals(fieldName)) {
            this.subject = value;
        } else if ("description".equals(fieldName)) {
            this.description = value;
        } else if ("from".equals(fieldName)) {
            // still TODO
        }
    }

}
