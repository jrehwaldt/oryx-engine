package de.hpi.oryxengine.resource.allocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;

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
    private Form form;

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

        this(subject, description, null, allocationStrategies, set);
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
    
    @SuppressWarnings("unchecked")
    public TaskImpl(String subject,
                    String description,
                    Form form,
                    AllocationStrategies allocationStrategies,
                    AbstractResource<?> assignedResource) {

        this(subject, description, form, allocationStrategies, new HashSet<AbstractResource<?>>(
            Arrays.asList(assignedResource)));
    }
    
    public TaskImpl(String subject,
                    String description,
                    Form form,
                    AllocationStrategies allocationStrategies,
                    Set<AbstractResource<?>> set) {

        this.subject = subject;
        this.description = description;
        this.form = form;
        this.allocationStrategies = allocationStrategies;
        this.assignedResources = set;
    }

    /**
     * Copy constructor.
     * 
     * @param taskToCopy
     *            the task to copy
     */
    public TaskImpl(Task taskToCopy) {

        this(taskToCopy.getSubject(),
             taskToCopy.getDescription(),
             taskToCopy.getForm(),
             taskToCopy.getAllocationStrategies(),
             new HashSet<AbstractResource<?>>(taskToCopy.getAssignedResources()));
        // it is very important that a new hash set is created here!!! Otherwise, two concurrently executing human task 
        // activities work on the same datastructure for assigned resources (which they manipulate; not cool).

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
    // It is a bit of a hack, but it is necessary because otherwise the form would be in the JSON, although it is
    // annotated as '@JsonIgnore' in the Task Interface.
    @JsonIgnore
    public Form getForm() {

        return form;
    }

    @Override
    public AllocationStrategies getAllocationStrategies() {

        return allocationStrategies;
    }

    @Override
    public Set<AbstractResource<?>> getAssignedResources() {

        if (assignedResources == null) {
            assignedResources = Collections.synchronizedSet(new HashSet<AbstractResource<?>>());
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
