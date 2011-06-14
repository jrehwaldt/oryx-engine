package org.jodaengine.resource.allocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jodaengine.resource.AbstractResource;

/**
 * This class helps to build {@link CreationPattern CreationPatterns}.
 */
public class CreationPatternBuilderImpl implements CreationPatternBuilder {

    private String taskSubject;
    private String taskDescription;
    private String formID;
    private List<AbstractResource<?>> abstractResources;

    /**
     * Instantiates a new creation pattern builder.
     */
    public CreationPatternBuilderImpl() {

        this.abstractResources = new ArrayList<AbstractResource<?>>();
        this.taskDescription = "";
        this.taskSubject = "";
        this.formID = null;
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
        @SuppressWarnings("rawtypes")
        Class[] parameterClasses = {String.class,
            String.class, 
            String.class, 
            List.class};
        CreationPattern pattern = null;
        Constructor<? extends CreationPattern> constructor = null;
        try {
            constructor = creationPatternClass.getConstructor(parameterClasses);
            pattern = constructor.newInstance(taskSubject, taskDescription, formID, resourcesCopy);
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return pattern;
    }

    @Override
    public CreationPatternBuilder flushAssignedResources() {

        this.abstractResources.clear();
        return this;
    }

}
