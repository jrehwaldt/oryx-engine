package de.hpi.oryxengine.resource;

import java.util.HashMap;
import java.util.Map;

import de.hpi.oryxengine.resource.worklist.EmptyWorklist;
import de.hpi.oryxengine.worklist.Worklist;

/**
 * ResourceImpl is the implementation of the {@link Resource} interface.
 * 
 * @param <R> - an interface that extends from Resource
 */
public class ResourceImpl<R extends Resource<?>> implements Resource<R> {

    /** The resource id. */
    protected String resourceId;
    
    protected ResourceType resourceType;
    
    /** The resource name. */
    protected String resourceName;
    
    /** The property table. */
    protected Map<String, Object> propertyTable;
    
    protected Worklist worklist;
    /**
     * Default Constructor.
     * 
     * @param id
     *            - the id of the resource
     */
    protected ResourceImpl(String id, ResourceType resourceType) {

        this.resourceType = resourceType;
        this.resourceId = id;
    }

    /**
     * Gets the id.
     *
     * @return the id
     * {@inheritDoc}
     */
    @Override
    public String getId() {

        return resourceId;
    }

    /**
     * Sets the id.
     *
     * @param id the id
     * @return the r
     * {@inheritDoc}
     */
    @Override
    public R setId(String id) {

        resourceId = id;
        return extractedThis();
    }

    /**
     * Gets the name.
     *
     * @return the name
     * {@inheritDoc}
     */
    @Override
    public String getName() {

        return resourceName;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     * @return the r
     * {@inheritDoc}
     */
    @Override
    public R setName(String name) {

        resourceName = name;
        return extractedThis();
    }

    /**
     * Gets the property.
     *
     * @param propertyId the property id
     * @return the property
     * {@inheritDoc}
     */
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

    /**
     * Sets the property.
     *
     * @param propertyKey the property key
     * @param propertyValue the property value
     * @return the r
     * {@inheritDoc}
     */
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
     * Two Resource objects are equal if their {@link ResourceType}s and their IDs an are the same.
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

        Resource<?> resourceToCompare = (Resource<?>) objectToCompare;
        
        // Only if the type and the id of the two objects are the same then it is true
        if (this.getType().equals(resourceToCompare.getType())) {
            if (this.getId().equals(resourceToCompare.getId())) {
                return true;
            }
        }
        // otherwise it should be false
        
        return false;
    }

    /**
     * The hashCode of a resource consists of the concatenated string of their type and their id.
     * 
     * @return Integer representing the hashCode
     * 
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {

        return (getType() + getId()).hashCode();
    }

    @Override
    public ResourceType getType() {

        return resourceType;
    }

    @Override
    public Worklist getWorklist() {

        return new EmptyWorklist();
    }
}
