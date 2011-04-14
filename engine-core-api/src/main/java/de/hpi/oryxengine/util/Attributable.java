package de.hpi.oryxengine.util;

import java.util.Map;

/**
 * This interface allows objects to define dynamic attributes.
 */
public interface Attributable {

    /**
     * Retrieves all variables stored for this object.
     * 
     * @return a {@link Map} containing the attributeKey and the corresponding value.
     */
    Map<String, Object> getAttributes();

    /**
     * Retrieves the value of an specified attribute with the specific key.
     * 
     * @param attributeKey
     *            of the attribute
     * @return the object stored behind the key 
     */
    Object getAttribute(String attributeKey);

    /**
     * Sets a certain attribute with its corresponding key and value.
     * 
     * @param attributeKey
     *            - key that identifies the attribute
     * @param attributeValue
     *            - value object that needs to be stored
     */
    void setAttribute(String attributeKey, Object attributeValue);

}
