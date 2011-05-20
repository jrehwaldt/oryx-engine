package org.jodaengine.ext.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list holding all available extensions.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-20
 *
 * @param <IExtension>
 */
public class ExtensionList<IExtension> implements Iterable<Class<IExtension>> {
    
    private List<Class<IExtension>> extensions;
    
    /**
     * Default constructor.
     */
    ExtensionList() {
        this.extensions = new ArrayList<Class<IExtension>>();
    }
    
    /**
     * Adds an extension type to the list of classes.
     * 
     * @param extension the extension type to add
     */
    void addExtensionType(Class<IExtension> extension) {
        this.extensions.add(extension);
    }
    
    /**
     * Provides a list of all available extension types.
     * 
     * @return a list of available extension types
     */
    List<Class<IExtension>> getExtensionTypes() {
        return this.extensions;
    }
    
    /**
     * Clear the list of extension types.
     */
    void clear() {
        this.extensions.clear();
    }
    
    @Override
    public Iterator<Class<IExtension>> iterator() {
        return this.extensions.iterator();
    }
}
