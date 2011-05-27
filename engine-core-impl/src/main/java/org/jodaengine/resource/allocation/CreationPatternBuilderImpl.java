package org.jodaengine.resource.allocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.pattern.creation.DirectDistributionPattern;
import org.jodaengine.resource.allocation.pattern.creation.RoleBasedDistributionPattern;

/**
 * This class helps to build {@link CreationPattern CreationPatterns}.
 */
public class CreationPatternBuilderImpl implements CreationPatternBuilder {

    private String taskSubject;
    private String taskDescription;
    private String formID;
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
    public CreationPatternBuilder setItemFormID(String formID) {

        this.formID = formID;
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
    public CreationPattern buildCreationPattern(Class<? extends CreationPattern> creationPatternClass) {

        List<AbstractResource<?>> resourcesCopy = new ArrayList<AbstractResource<?>>(abstractResources);
        Collections.copy(resourcesCopy, abstractResources);
        if (creationPatternClass.equals(RoleBasedDistributionPattern.class)) {
            return new RoleBasedDistributionPattern(taskSubject, taskDescription, formID, resourcesCopy);
        } else {
            return new DirectDistributionPattern(taskSubject, taskDescription, formID, resourcesCopy);
        }
    }

    @Override
    public CreationPatternBuilder flushAssignedResources() {

        this.abstractResources.clear();
        return this;
    }

}
