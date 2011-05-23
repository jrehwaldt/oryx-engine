package org.jodaengine.ext.service;

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
 *
 * @param <IInstance> the interface
 */
public class TypeSafeInstanceMap<IInstance> implements Iterable<Class<IInstance>> {
    
    private Map<Class<IInstance>, List<IInstance>> instances;
    
    /**
     * Default constructor.
     */
    TypeSafeInstanceMap() {
        this.instances = new HashMap<Class<IInstance>, List<IInstance>>();
    }
    
    /**
     * Adds any number of instances to the list of classes.
     * 
     * @param type the type
     * @param instances any number of instances
     */
    void addInstances(@Nonnull Class<IInstance> type,
                      @Nonnull IInstance ... instances) {
        
        //
        // create an empty list if necessary
        //
        if (!this.instances.containsKey(type)) {
            this.instances.put(type, new ArrayList<IInstance>());
        }
        
        List<IInstance> extensionInstances = this.instances.get(type);
        
        //
        // add all instances
        //
        extensionInstances.addAll(Arrays.asList(instances));
    }
    
    /**
     * Provides a list of all available instances.
     * 
     * @param type the type
     * @return a list of available instances
     */
    List<IInstance> getInstances(@Nonnull Class<IInstance> type) {
        return this.instances.get(type);
    }
    
    /**
     * Clear the list of instances.
     */
    void clear() {
        this.instances.clear();
    }
    
    @Override
    public Iterator<Class<IInstance>> iterator() {
        return this.instances.keySet().iterator();
    }
    
    /**
     * Provides an {@link Iterator} for instances of a certain type.
     * 
     * @param type the type
     * @return the instances iterator
     */
    public Iterator<IInstance> iterator(@Nonnull Class<IInstance> type) {
        return this.instances.get(type).iterator();
    }
}
