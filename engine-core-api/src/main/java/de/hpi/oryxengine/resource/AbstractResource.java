package de.hpi.oryxengine.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import de.hpi.oryxengine.resource.worklist.AbstractWorklist;
import de.hpi.oryxengine.resource.worklist.EmptyWorklist;
import de.hpi.oryxengine.util.Identifiable;

/**
 * Represents a resource that is part of the enterprise's organization structure.
 * 
 * It is the sup interface of all other organization elements.
 * 
 * @param <R>
 *            - extending Resource
 */
@XmlRootElement
//@XmlSeeAlso({
//    OrganizationUnit.class, Participant.class, Position.class, Role.class
//})
public abstract class AbstractResource<R extends AbstractResource<?>> implements Identifiable {

    protected UUID resourceId;
    protected ResourceType resourceType;

    protected String resourceName;

    protected Map<String, Object> propertyTable;
    protected AbstractWorklist worklist;
    
    /**
     * Hidden constructor with provided id.
     *
     * @param id - the id of the resource
     * @param resourceName the resource name
     * @param resourceType - the type of the {@link AbstractResource}
     */
    protected AbstractResource(@Nonnull UUID id,
                               @Nonnull String resourceName,
                               @Nonnull ResourceType resourceType) {

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
    protected AbstractResource(@Nonnull String resourceName,
                               @Nonnull ResourceType resourceType) {

        this(UUID.randomUUID(), resourceName, resourceType);
    }

    @Override
    public UUID getID() {

        return resourceId;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public @Nonnull String getName() {

        return resourceName;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            - the name
     * @return the current Resource object
     */
    public @Nonnull R setName(@Nonnull String name) {

        resourceName = name;
        return extractedThis();
    }

    /**
     * Gets the object that corresponds to the property id.
     * 
     * @param propertyId
     *            - the property id
     * @return the object corresponding to the property id
     */
    public @Nullable Object getProperty(@Nonnull String propertyId) {

        return getPropertyTable().get(propertyId);
    }

    /**
     * Gets the propertyTable.
     * 
     * @return the propertyTable
     */
    protected @Nonnull Map<String, Object> getPropertyTable() {

        if (propertyTable == null) {
            propertyTable = new HashMap<String, Object>();
        }
        return propertyTable;
    }
    
    /**
     * Stores a property that consists of a property id and the corresponding object.
     * 
     * @param propertyKey
     *            - the property key
     * @param propertyValue
     *            - the object that is stored to the property id
     * @return the current Resource object
     */
    public @Nonnull R setProperty(@Nonnull String propertyKey,
                                  @Nullable Object propertyValue) {

        getPropertyTable().put(propertyKey, propertyValue);
        return extractedThis();
    }

    /**
     * Extracts the current Object.
     * 
     * @return the current Object as instance of the sub class
     */
    @SuppressWarnings("unchecked")
    private @Nonnull R extractedThis() {

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
    public boolean equals(@Nullable Object objectToCompare) {
        
        if (objectToCompare == null) {
            return false;
        }
        
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

    /**
     * Returns the type of the {@link AbstractResource}. The type is an element of Enumeration {@link ResourceType}.
     * 
     * @return the type of the {@link AbstractResource}, which is an Element of the Enumeration
     */
    public @Nonnull ResourceType getType() {

        return resourceType;
    }
    /**
     * Retrieves the resource's worklist. 
     * 
     * @return the worklist of the resource
     */
    public @Nonnull AbstractWorklist getWorklist() {

        return new EmptyWorklist();
    }
}
