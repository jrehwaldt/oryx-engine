package org.jodaengine.ext.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * A list holding all instances available for a certain interface.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-20
 */
public class TypeSafeInstanceMap implements Iterable<Class<?>> {
    
    private Map<Class<?>, List<?>> instances;
    
    /**
     * Default constructor.
     */
    public TypeSafeInstanceMap() {
        this.instances = new HashMap<Class<?>, List<?>>();
    }
    
    /**
     * Adds any number of instances to the list of classes.
     * 
     * @param <IInstance> the instance's type
     * @param type the type
     * @param instances any number of instances
     */
    @SuppressWarnings("unchecked")
    public <IInstance> void addInstances(@Nonnull Class<IInstance> type,
                                         @Nonnull List<IInstance> instances) {
        
        this.addInstances(type, (IInstance[]) instances.toArray());
    }
    
    /**
     * Adds any number of instances to the list of classes.
     * 
     * @param <IInstance> the instance's type
     * @param type the type
     * @param instances any number of instances
     */
    public <IInstance> void addInstances(@Nonnull Class<IInstance> type,
                                         @Nonnull IInstance ... instances) {
        
        //
        // add all instances
        //
        List<IInstance> extensionInstances = getInstances(type);
        extensionInstances.addAll(Arrays.asList(instances));
    }
    
    /**
     * Provides a list of all available instances.
     * 
     * @param <IInstance> the instance's type
     * @param type the type
     * @return a list of available instances
     */
    @SuppressWarnings("unchecked")
    public @Nonnull <IInstance> List<IInstance> getInstances(@Nonnull Class<IInstance> type) {
        
        List<IInstance> result = (List<IInstance>) this.instances.get(type);
        
        //
        // create an empty list if necessary
        //
        if (!this.instances.containsKey(type)) {
            result = new ArrayList<IInstance>();
            this.instances.put(type, result);
        }
        
        return result;
    }
    
    /**
     * Returns whether a certain type is contained in this map.
     * 
     * @param <IInstance> the instance's type
     * @param type the type, which may be contained
     * @return true, if instances exist
     */
    public <IInstance> boolean containsInstances(@Nonnull Class<IInstance> type) {
        return getInstances(type).isEmpty();
    }
    
    /**
     * Clear the whole map of instances.
     */
    public void clear() {
        this.instances.clear();
    }
    
    @Override
    public @Nonnull Iterator<Class<?>> iterator() {
        return this.instances.keySet().iterator();
    }
    
    /**
     * Provides an {@link Iterator} for instances of a certain type.
     * 
     * @param <IInstance> the instance's type
     * @param type the type
     * @return the instances iterator
     */
    public @Nonnull <IInstance> Iterator<IInstance> iterator(@Nonnull Class<IInstance> type) {
        return getInstances(type).iterator();
    }
}
