package org.jodaengine.rest;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

/**
 * The Class PatchCollectionChangeset is used to access the PATCH methods of the REST API. As specified in <a
 * href="http://tools.ietf.org/html/rfc5789">the RFC 5789</a>, the update is to be communicated in some kind of
 * changeset. This is implemented here.
 * 
 * @param <T>
 *            the generic type of items, that are updated in the collection
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class PatchCollectionChangeset<T> {
    private List<T> removals;
    private List<T> additions;

    /**
     * Hidden constructor for deserialization.
     */
    protected PatchCollectionChangeset() {

    }

    /**
     * Gets the additions.
     * 
     * @return the additions
     */
    @JsonProperty("additions")
    public List<T> getAdditions() {

        return additions;
    }

    /**
     * Sets the additions.
     * 
     * @param additions
     *            the new additions
     */
    @JsonProperty("additions")
    public void setAdditions(List<T> additions) {

        this.additions = additions;
    }

    /**
     * Gets the removals.
     * 
     * @return the removals
     */
    @JsonProperty("removals")
    public List<T> getRemovals() {

        return removals;
    }

    /**
     * Sets the removals.
     * 
     * @param removals
     *            the new removals
     */
    @JsonProperty("removals")
    public void setRemovals(List<T> removals) {

        this.removals = removals;
    }

    // /**
    // * Returns a concrete resource object holding the value of the specified String.
    // * The argument is interpreted as json.
    // *
    // * @param json the json
    // * @return the patch collection changeset
    // */
    // public static PatchCollectionChangeset<T> valueOf(String json) {
    // TypeFactory.
    // JavaType typeRef = TypeFactory.collectionType(PathCollectionChangeset, elementType)
    // PatchCollectionChangeset<T> returnValue = ServiceFactory.getJsonMapper().readValue(json,
    // WorklistActionWrapper.class);
    // return returnValue;
    // }
}
