package de.hpi.oryxengine.resource.allocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.allocation.pattern.DirectDistributionPattern;
import de.hpi.oryxengine.resource.allocation.pattern.RoleDistributionPattern;

/**
 * This class helps to build {@link Task Tasks}.
 */
public class CreationPatternBuilderImpl implements CreationPatternBuilder {

    private String taskSubject;
    private String taskDescription;
    private Form taskForm;
    private List<AbstractResource<?>> abstractResources;

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
    public DirectDistributionPattern buildDirectDistributionPattern() {

        HashSet<AbstractResource<?>> assignedResources = new HashSet<AbstractResource<?>>(abstractResources);
        // TODO the array thing in the next line is not so nice.
        DirectDistributionPattern pattern = new DirectDistributionPattern(taskSubject, taskDescription, taskForm,
            abstractResources.toArray(new AbstractResource<?>[0]));
        return pattern;
    }

    @Override
    public RoleDistributionPattern buildRoleDistributionPattern() {

        // TODO Auto-generated method stub
        return null;
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
