package de.hpi.oryxengine.resource;

/**
 * Represents a resource that is part of the enterprise's organization structure.
 * 
 * It is the sup interface of all other organization elements.
 * 
 * @param <R>
 *            - extending Resource
 */
public interface Resource<R extends Resource<?>> {

    /**
     * Returns the type of the {@link Resource}. The type is an element of Enumeration {@link ResourceType}.
     * 
     * @return the type of the {@link Resource}
     */
    String getType();

    /**
     * Returns the id of the Resource.
     * 
     * @return the id of the Resource
     */
    String getId();

    /**
     * Sets the id.
     * 
     * @param id
     *            the id
     * @return the current Resource object
     */
    R setId(String id);

    /**
     * Gets the name.
     * 
     * @return the name
     */
    String getName();

    /**
     * Sets the name.
     * 
     * @param name
     *            - the name
     * @return the current Resource object
     */
    R setName(String name);

    /**
     * Gets the object that corresponds to the property id.
     * 
     * @param propertyId
     *            - the property id
     * @return the object corresponding to the property id
     */
    Object getProperty(String propertyId);

    /**
     * Stores a property that consists of a property id and the corresponding object.
     * 
     * @param propertyId
     *            - the property id
     * @param propertyValue
     *            - the object that is stored to the property id
     * @return the current Resource object
     */
    R setProperty(String propertyId, Object propertyValue);

}
