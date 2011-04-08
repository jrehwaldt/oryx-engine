package de.hpi.oryxengine.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import de.hpi.oryxengine.resource.worklist.EmptyWorklist;
import de.hpi.oryxengine.resource.worklist.Worklist;

/**
 * ResourceImpl is the implementation of the {@link AbstractResource} interface.
 * 
 * @param <R>
 *            - an interface that extends from Resource
 */
@XmlRootElement
@XmlSeeAlso({
    AbstractOrganizationUnit.class, AbstractParticipant.class, AbstractPosition.class, AbstractRole.class
})
public abstract class AbstractResourceImpl<R extends AbstractResource<?>> extends AbstractResource<R> {

    protected UUID resourceId;
    protected ResourceType resourceType;

    protected String resourceName;

    protected Map<String, Object> propertyTable;
    protected Worklist worklist;
    
    /**
     * Hidden constructor with provided id.
     *
     * @param id - the id of the resource
     * @param resourceName the resource name
     * @param resourceType - the type of the {@link AbstractResource}
     */
    protected AbstractResourceImpl(UUID id,
                           String resourceName,
                           ResourceType resourceType) {

        this.resourceId = id;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
    }

    /**
     * Constructor.
     *
     * @param resourceName - the resource name
     * @param resourceType - the type of the {@link AbstractResource}
     */
    protected AbstractResourceImpl(String resourceName, ResourceType resourceType) {

        this(UUID.randomUUID(), resourceName, resourceType);
    }

    @Override
    public UUID getID() {

        return resourceId;
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
    public R setProperty(String propertyKey,
                         Object propertyValue) {

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

        if (!(objectToCompare instanceof AbstractResource<?>)) {
            return super.equals(objectToCompare);
        }

        AbstractResource<?> resourceToCompare = (AbstractResource<?>) objectToCompare;

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
     * {@inheritDoc}
     * 
     * @return Integer representing the hashCode
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
