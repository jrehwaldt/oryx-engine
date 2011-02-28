package de.hpi.oryxengine.util;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * This interface declares certain entities as identifiable
 * and therefore requires them to provide a getter for it's ID.
 * 
 */
public interface Identifiable {
    
    /**
     * Gets the ID.
     *
     * @return the ID
     */
    UUID getID();
}
