package de.hpi.oryxengine.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * ResourceImpl is the implementation of the {@link Resource} interface.
 * 
 * @param <R> - an interface that extends from Resource
 */
public class ResourceImpl<R extends Resource<?>> implements Resource<R> {

    protected String resourceId;
    protected ResourceType resourceType;      
    protected String resourceName;
    
    protected Map<String, Object> propertyTable;

    /**
     * Default Constructor.
     * 
     * @param id
     *            - the id of the resource
     */
    protected ResourceImpl(String id) {

        resourceId = id;
    }

    @Override
    public String getId() {

        return resourceId;
    }

    @Override
    public R setId(String id) {

        resourceId = id;
        return extractedThis();
    }

    @Override
    public String getName() {

        return resourceName;
    }

    @Override
    public R setName(String name) {

        resourceName = name;
        return extractedThis();
    }

    @Override
    public Object getProperty(String propertyId) {

        return getPropertyTable().get(propertyId);
    }

    /**
     * Gets the propertyTable.
     *
     * @return the propertyTable
     */
    protected Map<String, Object> getPropertyTable() {

        if (propertyTable == null) {
            propertyTable = new HashMap<String, Object>();
        }
        return propertyTable;
    }

    @Override
    public R setProperty(String propertyKey, Object propertyValue) {

        getPropertyTable().put(propertyKey, propertyValue);
        return extractedThis();
    }

    /**
     * Extracts the current Object.
     * 
     * @return the current Object as instance of the sub class
     */
    @SuppressWarnings("unchecked")
    private R extractedThis() {

        return (R) this;
    }

    /**
     * Two Resource objects are equal if IDs are the same.
     * 
     * 
     * @param objectToCompare
     *            - if the object is not a Resource object then it is treated like any other object
     * @return Boolean - saying if the object is the same or not
     */
    @Override
    public boolean equals(Object objectToCompare) {

        if (!(objectToCompare instanceof Resource<?>)) {
            return super.equals(objectToCompare);
        }

        Resource<?> positionToCompare = (Resource<?>) objectToCompare;
        return this.getId().equals(positionToCompare.getId());
    }

    @Override
    public int hashCode() {

        return getId().hashCode();
    }

    @Override
    public String getType() {

        return null;
    }
}
