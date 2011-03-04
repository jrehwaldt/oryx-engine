package de.hpi.oryxengine.identity;

/**
 * Represents a resource that is part of the enterprise's organization strucuture.
 * @param <R> extending Resource
 * @author Gerardo Navarro Suarez
 */
public interface Resource<R extends Resource<?>> {

    String getId();
    R setId(String id);

    String getName();
    R setName(String name);
    
    Object getProperty(String propertyId);
    R setProperty(String propertyId, Object propertyValue);
    
}
