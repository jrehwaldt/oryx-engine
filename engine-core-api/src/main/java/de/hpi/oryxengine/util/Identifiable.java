package de.hpi.oryxengine.util;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This interface declares certain entities as identifiable
 * and therefore requires them to provide a getter for it's ID.
 */
public interface Identifiable {
    
    /**
     * Gets the ID.
     *
     * @return the ID
     */
    @JsonProperty
    @Nonnull UUID getID();
}
