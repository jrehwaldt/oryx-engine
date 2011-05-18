package org.jodaengine.util;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This interface declares certain entities as identifiable
 * and therefore requires them to provide a getter for it's ID.
 *
 * @param <T> the generic type that is used for identification
 */
public interface Identifiable<T> {

    /**
     * Gets the ID.
     * 
     * @return the ID
     */
    @JsonProperty
    @Nonnull
    T getID();
}
