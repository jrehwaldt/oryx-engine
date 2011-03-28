package de.hpi.oryxengine.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.resource.worklist.EmptyWorklist;
import de.hpi.oryxengine.resource.worklist.Worklist;

/**
 * ResourceImpl is the implementation of the {@link Resource} interface.
 * 
 * @param <R>
 *            - an interface that extends from Resource
 */
public class ResourceImpl<R extends Resource<?>> implements Resource<R> {

    /** The resource id. */
    protected UUID resourceId;

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
     * @param resourceType
     *            - the type of the {@link Resource}
     */
    protected ResourceImpl(UUID id, String resourceName, ResourceType resourceType) {

        this.resourceId = id;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
    }

    protected ResourceImpl(String resourceName, ResourceType resourceType) {

        this(UUID.randomUUID(), resourceName, resourceType);
    }

    @Override
    public UUID getID() {

        return resourceId;
    }

    /**
     * Gets the name.
     * 
     * @return the name {@inheritDoc}
     */
    @Override
    public String getName() {

        return resourceName;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the name
     * @return the r {@inheritDoc}
     */
    @Override
    public R setName(String name) {

        resourceName = name;
        return extractedThis();
    }

    /**
     * Gets the property.
     * 
     * @param propertyId
     *            the property id
     * @return the property {@inheritDoc}
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
     * @param propertyKey
     *            the property key
     * @param propertyValue
     *            the property value
     * @return the r {@inheritDoc}
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
            if (this.getID().equals(resourceToCompare.getID())) {
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
     *         {@inheritDoc}
     */
    @Override
    public int hashCode() {

        return (getType().toString() + getID().toString()).hashCode();
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
