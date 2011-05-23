package org.jodaengine.ext.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list holding all types available for a certain interface.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-20
 *
 * @param <IInterface> the interface
 */
public class TypeSafeList<IInterface> implements Iterable<Class<IInterface>> {
    
    private List<Class<IInterface>> types;
    
    /**
     * Default constructor.
     */
    TypeSafeList() {
        this.types = new ArrayList<Class<IInterface>>();
    }
    
    /**
     * Adds a type to the list of types.
     * 
     * @param type the type to add
     */
    void addType(Class<IInterface> type) {
        this.types.add(type);
    }
    
    /**
     * Provides a list of all available types.
     * 
     * @return a list of available types
     */
    List<Class<IInterface>> getTypes() {
        return this.types;
    }
    
    /**
     * Clear the list of types.
     */
    void clear() {
        this.types.clear();
    }
    
    @Override
    public Iterator<Class<IInterface>> iterator() {
        return this.types.iterator();
    }
}
