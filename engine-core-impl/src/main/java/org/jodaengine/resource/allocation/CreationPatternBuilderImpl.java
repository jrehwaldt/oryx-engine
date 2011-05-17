package org.jodaengine.resource.allocation;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.allocation.Form;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;

/**
 * This class helps to build {@link CreationPattern CreationPatterns}.
 */
public class CreationPatternBuilderImpl implements CreationPatternBuilder {

    private String taskSubject;
    private String taskDescription;
    private Form taskForm;
    private List<AbstractResource<?>> abstractResources;

    /**
     * Instantiates a new creation pattern builder impl.
     */
    public CreationPatternBuilderImpl() {

        this.abstractResources = new ArrayList<AbstractResource<?>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreationPatternBuilder setItemSubject(String taskSubject) {

        this.taskSubject = taskSubject;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreationPatternBuilder setItemDescription(String taskDescription) {

        this.taskDescription = taskDescription;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreationPatternBuilder setItemForm(Form taskForm) {

        this.taskForm = taskForm;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreationPatternBuilder addResourceAssignedToItem(AbstractResource<?> resourceAssignedToTask) {

        this.abstractResources.add(resourceAssignedToTask);
        return this;
    }

    @Override
    public ConcreteResourcePattern buildConcreteResourcePattern() {

        // TODO the array thing in the next line is not so nice.
        ConcreteResourcePattern pattern = new ConcreteResourcePattern(taskSubject, taskDescription, taskForm,
            abstractResources.toArray(new AbstractResource<?>[0]));
        return pattern;
    }

    @Override
    public CreationPatternBuilder flushAssignedResources() {

        this.abstractResources.clear();
        return this;
    }

    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // public Task buildTask() {
    //
    // AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(taskPushPattern, taskPullPattern, null);
    // HashSet<AbstractResource<?>> assignedResources = new HashSet<AbstractResource<?>>(abstractResources);
    // Task resultTask = new TaskImpl(taskSubject, taskDescription, taskForm, allocationStrategies, assignedResources);
    //
    // return resultTask;
    // }

    // @Override
    // public CreationPattern buildCreationPattern() {
    //
    // HashSet<AbstractResource<?>> assignedResources = new HashSet<AbstractResource<?>>(abstractResources);
    // return new
    // return null;
    // }

}
