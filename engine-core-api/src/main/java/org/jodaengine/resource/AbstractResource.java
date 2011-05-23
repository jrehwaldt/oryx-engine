package org.jodaengine.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.ObjectMapper;
import org.jodaengine.resource.worklist.AbstractWorklist;
import org.jodaengine.resource.worklist.EmptyWorklist;
import org.jodaengine.util.Identifiable;

/**
 * Represents a resource that is part of the enterprise's organization structure.
 * 
 * It is the sup interface of all other organization elements.
 * 
 * @param <R>
 *            - extending Resource
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public abstract class AbstractResource<R extends AbstractResource<?>> implements Identifiable<UUID> {

    protected UUID id;

    protected ResourceType type;

    protected String name;

    protected Map<String, String> propertyTable;

    protected AbstractWorklist worklist;

    /**
     * Hidden jaxb constructor.
     */
    protected AbstractResource() {

    }

    /**
     * Hidden constructor with provided id.
     * 
     * @param id
     *            - the id of the resource
     * @param resourceName
     *            the resource name
     * @param resourceType
     *            - the type of the {@link AbstractResource}
     */
    protected AbstractResource(@Nonnull UUID id, @Nonnull String resourceName, @Nonnull ResourceType resourceType) {

        this.id = id;
        this.name = resourceName;
        this.type = resourceType;
    }

    /**
     * Constructor.
     * 
     * @param resourceName
     *            - the resource name
     * @param resourceType
     *            - the type of the {@link AbstractResource}
     */
    protected AbstractResource(@Nonnull String resourceName, @Nonnull ResourceType resourceType) {

        this(UUID.randomUUID(), resourceName, resourceType);
    }

    @JsonProperty
    @Override
    public UUID getID() {

        return id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    @JsonProperty
    public @Nonnull
    String getName() {

        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            - the name
     * @return the current Resource object
     */
    public @Nonnull
    R setName(@Nonnull String name) {

        this.name = name;
        return extractedThis();
    }

    /**
     * Gets the object that corresponds to the property id.
     * 
     * @param propertyId
     *            - the property id
     * @return the object corresponding to the property id
     */
    public @Nullable
    Object getProperty(@Nonnull String propertyId) {

        return getPropertyTable().get(propertyId);
    }

    /**
     * Gets the propertyTable.
     * 
     * @return the propertyTable
     */
    @JsonProperty
    protected @Nonnull
    Map<String, String> getPropertyTable() {

        if (propertyTable == null) {
            propertyTable = new HashMap<String, String>();
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
    public @Nonnull
    R setProperty(@Nonnull String propertyKey, @Nullable String propertyValue) {

        getPropertyTable().put(propertyKey, propertyValue);
        return extractedThis();
    }

    /**
     * Extracts the current Object.
     * 
     * @return the current Object as instance of the sub class
     */
    @SuppressWarnings("unchecked")
    private @Nonnull
    R extractedThis() {

        return (R) this;
    }

    /**
     * Two Resource objects are equal if their {@link ResourceType}s and their IDs an are the same.
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
    @JsonProperty
    public @Nonnull
    ResourceType getType() {

        return type;
    }

    /**
     * Retrieves the resource's worklist.
     * 
     * @return the worklist of the resource
     */
    @JsonProperty
    public @Nonnull
    AbstractWorklist getWorklist() {

        return new EmptyWorklist();
    }

    /**
     * Sets any other type not recognized by JSON serializer.
     * This method is indented to be used by Jackson.
     * 
     * @param fieldName
     *            - the fieldName
     * @param value
     *            - the value
     */
    @JsonAnySetter
    protected void setOtherJson(@Nonnull String fieldName, @Nullable String value) {

        if ("type".equals(fieldName)) {
            this.type = ResourceType.valueOf(value);
        } else if ("id".equals(fieldName)) {
            this.id = UUID.fromString(value);
        }
    }

    /**
     * Sets a worklist. Used for Jackson deserialization only.
     * 
     * @param worklist
     *            - the worklist
     */
    protected void setWorklist(@Nonnull AbstractWorklist worklist) {

        this.worklist = worklist;
    }

    /**
     * Returns a concrete resource object holding the value of the specified String.
     * The argument is interpreted as json.
     * 
     * @param json
     *            json formated object
     * @return a concrete resource
     * @throws IOException
     *             failed to parse the json
     */
    public static @Nonnull
    AbstractResource<?> valueOf(@Nonnull String json)
    throws IOException {

        // TODO we do not have access to the ServiceFactory, so we need a new ObjectMapper every time
        return (AbstractResource<?>) new ObjectMapper().readValue(json, AbstractResource.class);
    }
}
